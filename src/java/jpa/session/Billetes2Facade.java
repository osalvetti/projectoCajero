/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entidades.Billetes2;

/**
 *
 * @author oscarhsalvettig
 */
@Stateless
public class Billetes2Facade extends AbstractFacade<Billetes2> {

    @PersistenceContext(unitName = "CajeroPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Billetes2Facade() {
        super(Billetes2.class);
    }
    
}
