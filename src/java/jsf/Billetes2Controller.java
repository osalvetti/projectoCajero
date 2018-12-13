package jsf;

import exceptions.CajeroException;
import jpa.entidades.Billetes2;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.Billetes2Facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("billetes2Controller")
@SessionScoped
public class Billetes2Controller implements Serializable {

    private Billetes2 current;
    private DataModel items = null;
    
    @EJB
    private jpa.session.Billetes2Facade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int valorRetiro;
    
    private List<Billetes2> lista = new ArrayList();
    private int totalCajero;
    private int totalDiez;
    private int totalVeinte;
    private int totalCincuenta;
    private int valorARetirar;
    
    private int cantidadCincuenta = 0;
    private int cantidadVeinte = 0;
    private int cantidadDiez = 0;
    private String resultado;
    private String resultado1;

    public Billetes2Controller() {
    }

    public Billetes2 getSelected() {
        if (current == null) {
            current = new Billetes2();
            selectedItemIndex = -1;
        }
        return current;
    }

    private Billetes2Facade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Billetes2) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Billetes2();
        selectedItemIndex = -1;
        return "Create";
    }
    
    
    public String PreRetiro() {

        lista = getFacade().findAll();
        try {
            for (Billetes2 entidad : lista) {

                if (entidad.getDenominacion() == 10000) {
                    totalDiez = entidad.getDenominacion() * entidad.getCantidad();
                }
                if (entidad.getDenominacion() == 20000) {
                    totalVeinte = entidad.getDenominacion() * entidad.getCantidad();
                }
                if (entidad.getDenominacion() == 50000) {
                    totalCincuenta = entidad.getDenominacion() * entidad.getCantidad();
                }
            }
            valorARetirar = getValorRetiro();
            totalCajero = totalDiez + totalVeinte + totalCincuenta;
            if (totalCajero < valorARetirar) {
                 throw new CajeroException(ResourceBundle.getBundle("/Bundle").getString("error_11"));
            }
            validarCantidadxDenominacion();
            realizarTransaccion();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, e.getMessage());
          
        }
        
        return "Retiro";
    }
    
    public String validarCantidadxDenominacion() throws Exception{
        cantidadCincuenta = 0;
        cantidadVeinte = 0;
        int cantidadDisponible = 0;
            while (true) {
                if (totalCincuenta > 0) {
                    cantidadCincuenta = valorARetirar / 50000;
                    cantidadDisponible = totalCincuenta / 50000;
                    if (cantidadDisponible >= cantidadCincuenta) {
                        valorARetirar = valorARetirar - (cantidadCincuenta * 50000);
                        if (valorARetirar == 0) {
                            break;
                        }
                    } else {
                        cantidadCincuenta = cantidadDisponible;
                        valorARetirar = valorARetirar - (cantidadCincuenta * 50000);
                    }
                }
                if (totalVeinte > 0) {
                    cantidadVeinte = valorARetirar / 20000;
                    cantidadDisponible = totalVeinte / 20000;
                    if (cantidadDisponible >= cantidadVeinte) {
                        valorARetirar = valorARetirar - (cantidadVeinte * 20000);
                        if (valorARetirar == 0) {
                            break;
                        }
                    } else {
                        cantidadVeinte = cantidadDisponible;
                        valorARetirar = valorARetirar - (cantidadVeinte * 20000);
                        break;
                    }

                }
                if (totalDiez > 0) {
                    cantidadDiez = valorARetirar / 10000;
                    cantidadDisponible = totalDiez / 10000;
                    if (cantidadDisponible >= cantidadDiez) {
                        valorARetirar = valorARetirar - ( cantidadDiez * 10000);
                        if (valorARetirar == 0) {
                            break;
                        }
                    } else{
                        throw new CajeroException(ResourceBundle.getBundle("/Bundle").getString("error_12"));
                    }
                }else {
                    throw new CajeroException(ResourceBundle.getBundle("/Bundle").getString("error_10"));
                }

            }

        
        return "Retiro";
        
    }

    
    
    
    public String preCreate() {
        try {
            validaNulosDenominacion();
            if (current.getDenominacion() != 10000 && current.getDenominacion() != 20000 && current.getDenominacion() != 50000) {
                throw new Exception();
            }
            Billetes2 encontrado;
            int cantidadAdd;
            encontrado = getFacade().find(current.getDenominacion());
            if (encontrado == null) {
                getFacade().create(current);
                items = getPagination().createPageDataModel();
                current = new Billetes2();
                selectedItemIndex = -1;
                return null;
            } else {
                cantidadAdd = current.getCantidad();
                cantidadAdd = cantidadAdd + encontrado.getCantidad();
                current.setCantidad(cantidadAdd);
                this.update();

            }
            items = getPagination().createPageDataModel();
            current = new Billetes2();
            selectedItemIndex = -1;
            return null;

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("InvalidDenominacionError"));
            return null;
        }
        
    }
    
    public String validaNulosDenominacion(){
        
        try{    
            if (current.getDenominacion() == null) {
                throw new Exception();
            }
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("CreateBilletes2RequiredMessage_denominacion"));
            return null;
        }
        return null;
    }

    public String create() {
        try {
            Billetes2 encontrado;
            encontrado = getFacade().find(current.getDenominacion());
            if(encontrado==null){
                getFacade().create(current);
            }else{
                this.update();
            }
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Billetes2Created"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Billetes2) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Billetes2Updated"));
            return "index";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Billetes2) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Billetes2Deleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Billetes2 getBilletes2(java.lang.String id) {
        return ejbFacade.find(id);
    }

    public Integer getValorRetiro() {
        return valorRetiro;
    }

    public void setValorRetiro(Integer valorRetiro) {
        this.valorRetiro = valorRetiro;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado1() {
        return resultado1;
    }

    public void setResultado1(String resultado1) {
        this.resultado1 = resultado1;
    }
    
    
    
    
    
    
    private String realizarTransaccion(){
        int cantidadAct = 0;
        int cantidadRes = 0;
        String res1 = "";
        String res2 = "";
        String res3 = "";
        if(cantidadCincuenta>0){
            current = new Billetes2();
            Billetes2 entidad = new Billetes2();
            entidad = getFacade().find(50000);
            cantidadRes = entidad.getCantidad() - cantidadCincuenta;
            entidad.setDenominacion(cantidadRes);
            current.setDenominacion(50000);
            current.setCantidad(cantidadRes);
            res3 = cantidadCincuenta + " (50.000) ";
            update();
        }
        cantidadRes = 0;
        if(cantidadVeinte>0){
            current = new Billetes2();
            Billetes2 entidad = new Billetes2();
            entidad = getFacade().find(20000);
            cantidadRes = entidad.getCantidad() - cantidadVeinte;
            entidad.setDenominacion(cantidadRes);
            current.setDenominacion(20000);
            current.setCantidad(cantidadRes);
            res2 = cantidadVeinte + " (20.000) ";
            update();
        }
        cantidadRes = 0;
        if(cantidadDiez>0){
            current = new Billetes2();
            Billetes2 entidad = new Billetes2();
            entidad = getFacade().find(10000);
            cantidadRes = entidad.getCantidad() - cantidadDiez;
            entidad.setDenominacion(cantidadRes);
            current.setDenominacion(10000);
            current.setCantidad(cantidadRes);
            res1 = cantidadDiez + " (10.000) ";
            update();
        }
        resultado = "         Valor Solicitado: " + valorRetiro; 
        resultado1 = "Billetes Entregados: " + res1 + res2 + res3;
        valorRetiro = 0;
        return null;
    }
    
    public void irAdmon(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getApplication().getNavigationHandler().handleNavigation(context, null, "/index.xhtml");
        items = getPagination().createPageDataModel();
        current = new Billetes2();
        selectedItemIndex = -1;
       
    }
    
    public void irCajero(){
        resultado = "";
        resultado1 = "";
        FacesContext context = FacesContext.getCurrentInstance();
        context.getApplication().getNavigationHandler().handleNavigation(context, null, "/Retiro.xhtml");
        items = getPagination().createPageDataModel();
        current = new Billetes2();
        selectedItemIndex = -1;
        
    }
    
    
    

    @FacesConverter(forClass = Billetes2.class)
    public static class Billetes2ControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            Billetes2Controller controller = (Billetes2Controller) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "billetes2Controller");
            return controller.getBilletes2(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Billetes2) {
                Billetes2 o = (Billetes2) object;
                return getStringKey(o.getDenominacion().toString());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Billetes2.class.getName());
            }
        }

    }

}
