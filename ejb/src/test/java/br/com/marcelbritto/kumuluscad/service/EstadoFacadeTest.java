package br.com.marcelbritto.kumuluscad.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJBException;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.marcelbritto.kumuluscad.dao.EstadoDAO;
import br.com.marcelbritto.kumuluscad.exception.BusinessException;
import br.com.marcelbritto.kumuluscad.model.Estado;

public class EstadoFacadeTest {

    @Mock
    private Logger logger;

    @Mock
    private EstadoDAO estadoDAO;

    @InjectMocks
    private EstadoFacade estadoFacade;

    private ResourceBundle bundle;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bundle = ResourceBundle.getBundle("META-INF/ApplicationResources");
    }

    @Test
    public void testListStates_Success() throws BusinessException {
        List<Estado> estados = new ArrayList<>();
        estados.add(new Estado(1, "São Paulo"));
        estados.add(new Estado(2, "Rio de Janeiro"));

        when(estadoDAO.getList()).thenReturn(estados);

        List<Estado> result = estadoFacade.listStates();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("São Paulo", result.get(0).getNome());
        assertEquals("Rio de Janeiro", result.get(1).getNome());

        verify(logger, never()).error(anyString(), any(Throwable.class));
    }

    @Test
    public void testListStates_Exception() {
        when(estadoDAO.getList()).thenThrow(new EJBException("Database connection error"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            estadoFacade.listStates();
        });

        assertEquals(bundle.getString("error_load_states"), exception.getMessage());
        verify(logger).error(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetState_Success() throws BusinessException {
        Estado estado = new Estado(1, "São Paulo");

        when(estadoDAO.find(1)).thenReturn(estado);

        Estado result = estadoFacade.getState(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("São Paulo", result.getNome());

        verify(logger, never()).error(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetState_Exception() {
        when(estadoDAO.find(1)).thenThrow(new EJBException("Database connection error"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            estadoFacade.getState(1);
        });

        assertEquals(bundle.getString("error_load_state"), exception.getMessage());
        verify(logger).error(anyString(), any(Throwable.class));
    }
}
