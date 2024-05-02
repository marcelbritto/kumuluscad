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

import br.com.marcelbritto.kumuluscad.dao.CidadeDAO;
import br.com.marcelbritto.kumuluscad.exception.BusinessException;
import br.com.marcelbritto.kumuluscad.model.Cidade;
import br.com.marcelbritto.kumuluscad.model.Estado;

public class CidadeFacadeTest {

    @Mock
    private Logger logger;

    @Mock
    private CidadeDAO cidadeDAO;

    @InjectMocks
    private CidadeFacade cidadeFacade;

    private ResourceBundle bundle;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bundle = ResourceBundle.getBundle("META-INF/ApplicationResources");
    }

    @Test
    public void testListCities_Success() throws BusinessException {
        List<Cidade> cidades = new ArrayList<>();
        cidades.add(new Cidade(1, "São Paulo"));
        cidades.add(new Cidade(2, "Rio de Janeiro"));

        when(cidadeDAO.getList()).thenReturn(cidades);

        List<Cidade> result = cidadeFacade.listCities();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("São Paulo", result.get(0).getNome());
        assertEquals("Rio de Janeiro", result.get(1).getNome());

        verify(logger, never()).error(anyString(), any(Throwable.class));
    }

    @Test
    public void testListCities_Exception() {
        when(cidadeDAO.getList()).thenThrow(new EJBException("Database connection error"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cidadeFacade.listCities();
        });

        assertEquals(bundle.getString("error_load_cities"), exception.getMessage());
        verify(logger).error(anyString(), any(Throwable.class));
    }

    @Test
    public void testListCitiesByEstado_Success() throws BusinessException {
        Estado estado = new Estado(1, "São Paulo");

        List<Cidade> cidades = new ArrayList<>();
        cidades.add(new Cidade(1, "Campinas"));
        cidades.add(new Cidade(2, "São Paulo"));

        when(cidadeDAO.getObjects("Cidade.findByEstado", new String[]{"estado"}, new Integer[]{estado.getId()}))
                .thenReturn(cidades);

        List<Cidade> result = cidadeFacade.listCities(estado);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Campinas", result.get(0).getNome());
        assertEquals("São Paulo", result.get(1).getNome());

        verify(logger, never()).error(anyString(), any(Throwable.class));
    }

    @Test
    public void testListCitiesByEstado_Exception() {
        Estado estado = new Estado(1, "São Paulo");

        when(cidadeDAO.getObjects("Cidade.findByEstado", new String[]{"estado"}, new Integer[]{estado.getId()}))
                .thenThrow(new EJBException("Database connection error"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cidadeFacade.listCities(estado);
        });

        assertEquals(bundle.getString("error_load_cities"), exception.getMessage());
        verify(logger).error(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetCidade_Success() throws BusinessException {
        Cidade cidade = new Cidade(1, "São Paulo");

        when(cidadeDAO.find(1)).thenReturn(cidade);

        Cidade result = cidadeFacade.getCidade(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("São Paulo", result.getNome());

        verify(logger, never()).error(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetCidade_Exception() {
        when(cidadeDAO.find(1)).thenThrow(new EJBException("Database connection error"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cidadeFacade.getCidade(1);
        });

        assertEquals(bundle.getString("error_load_city"), exception.getMessage());
        verify(logger).error(anyString(), any(Throwable.class));
    }
}
