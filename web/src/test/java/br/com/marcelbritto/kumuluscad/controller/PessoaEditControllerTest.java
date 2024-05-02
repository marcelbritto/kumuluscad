package br.com.marcelbritto.kumuluscad.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.marcelbritto.kumuluscad.exception.BusinessException;
import br.com.marcelbritto.kumuluscad.model.Cidade;
import br.com.marcelbritto.kumuluscad.model.Endereco;
import br.com.marcelbritto.kumuluscad.model.Estado;
import br.com.marcelbritto.kumuluscad.model.Pessoa;
import br.com.marcelbritto.kumuluscad.service.EstadoFacade;
import br.com.marcelbritto.kumuluscad.service.PessoaFacade;

public class PessoaEditControllerTest {

    @Mock
    private Logger logger;

    @Mock
    private FacesContext facesContext;

    @Mock
    private PessoaFacade pessoaFacade;

    @Mock
    private EstadoFacade estadoFacade;
    
    @Mock
    private ExternalContext mockExternalContext;

//    @InjectMocks
    private PessoaEditController pessoaEditController;

    private List<String> errorList;

    @BeforeEach
    public void setup() {
    	MockitoAnnotations.openMocks(this);
        
        errorList = new ArrayList<>();
        ResourceBundle mockBundle = mock(ResourceBundle.class);
        pessoaEditController = new PessoaEditController(facesContext, mockBundle);
        pessoaEditController.setFacade(pessoaFacade);
        pessoaEditController.setLogger(logger);
        pessoaEditController.setEstadoFacade(estadoFacade);
    }

    @Test
    public void testInsertEndereco() {
        Endereco endereco = new Endereco();
        Estado estado = new Estado();
        estado.setInitial("SP");
        Cidade cidade = new Cidade();
        cidade.setNome("São Paulo");
        pessoaEditController.setSelectedEstado(estado);
        pessoaEditController.setSelectedCidade(cidade);
        pessoaEditController.setEnderecoEdit(endereco);
        pessoaEditController.setSelectedEnderecoList(new HashSet<>());

        pessoaEditController.insertEndereco();

        assertTrue(pessoaEditController.getSelectedEnderecoList().contains(endereco));
        assertEquals("SP", endereco.getEstado());
        assertEquals("São Paulo", endereco.getCidade());
        assertNotNull(pessoaEditController.getEnderecoEdit());
    }

    @Test
    public void testDeleteEndereco() {
        Endereco endereco = new Endereco();
        Set<Endereco> enderecos = new HashSet<>();
        enderecos.add(endereco);
        pessoaEditController.setSelectedEndereco(endereco);
        pessoaEditController.setSelectedEnderecoList(enderecos);

        pessoaEditController.deleteEndereco();

        assertTrue(pessoaEditController.getSelectedEnderecoList().isEmpty());
    }

    @Test
    public void testSave_Success() throws BusinessException {
        Pessoa pessoa = new Pessoa();
        pessoaEditController.setPessoa(pessoa);
        pessoaEditController.setInsertMode(true);
        pessoaEditController.save();

        verify(pessoaFacade).validateRequiredFields(pessoa, List.of());
        verify(pessoaFacade).create(pessoa);
    }
    
    @Test
    public void testUpdate_Success() throws BusinessException {
        Pessoa pessoa = new Pessoa();
        pessoaEditController.setPessoa(pessoa);
        pessoaEditController.setInsertMode(false);
        pessoaEditController.save();

        verify(pessoaFacade).validateRequiredFields(pessoa, List.of());
        verify(pessoaFacade).update(pessoa);
    }
    

    @Test
    public void testSelectedStateListener() throws BusinessException {
        Estado estado = new Estado();
        pessoaEditController.setSelectedEstado(estado);

        pessoaEditController.selectedStateListener();

        verify(pessoaFacade).listCidades(estado);
    }
    
    
}
