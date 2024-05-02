package br.com.marcelbritto.kumuluscad.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.application.FacesMessage;
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

public class PessoaEditControllerTest2 {

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
     // Configuração do FacesContext simulado
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);

        // Configuração do ResourceBundle simulado
        ResourceBundle mockBundle = mock(ResourceBundle.class);
        when(facesContext.getApplication().getResourceBundle(facesContext, "msgs")).thenReturn(mockBundle);

        // Configuração do ExternalContext simulado
        when(facesContext.getExternalContext()).thenReturn(mockExternalContext);

        // Configuração do método getFlash().get("pessoa") simulado
        Pessoa mockPessoa = new Pessoa(); // Simulando uma Pessoa existente
        when(mockExternalContext.getFlash().get("pessoa")).thenReturn(mockPessoa);

        // Inicialização do PessoaEditController
        pessoaEditController = new PessoaEditController();
        pessoaEditController.setLogger(logger);
//        pessoaEditController.setContext(facesContext);
    }

    @Test
    public void testInit_NoExistingPerson() {
        // Simulate init with no existing person
        pessoaEditController.init();

        assertTrue(pessoaEditController.isInsertMode());
        assertNull(pessoaEditController.getPessoa());
        assertTrue(pessoaEditController.getSelectedEnderecoList().isEmpty());
        assertNotNull(pessoaEditController.getEnderecoEdit());
        assertNotNull(pessoaEditController.getSelectedEndereco());
        assertNotNull(pessoaEditController.getSelectedCidade());
        assertNotNull(pessoaEditController.getSelectedEstado());
        assertTrue(pessoaEditController.getSelectedEstado().getCidades().isEmpty());
    }

    @Test
    public void testInit_ExistingPerson() throws BusinessException {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);
        Set<Endereco> enderecos = new HashSet<>();
        enderecos.add(new Endereco());
        pessoa.setEnderecos(enderecos);

        when(facesContext.getExternalContext().getFlash().get("pessoa")).thenReturn(pessoa);
        when(pessoaFacade.listEndereco(pessoa)).thenReturn(enderecos);

        pessoaEditController.init();

        assertFalse(pessoaEditController.isInsertMode());
        assertNotNull(pessoaEditController.getPessoa());
        assertEquals(1, pessoaEditController.getPessoa().getId());
        assertEquals(enderecos, pessoaEditController.getSelectedEnderecoList());
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
        pessoaEditController.setSelectedEnderecoList(enderecos);

        pessoaEditController.deleteEndereco();

        assertTrue(pessoaEditController.getSelectedEnderecoList().isEmpty());
    }

    @Test
    public void testSave_Success() throws BusinessException {
        Pessoa pessoa = new Pessoa();
        pessoaEditController.setPessoa(pessoa);

        pessoaEditController.save();

        verify(pessoaFacade).validateRequiredFields(pessoa, List.of());
        verify(pessoaFacade).create(pessoa);
    }

    @Test
    public void testSave_WithErrors() throws BusinessException {
        Pessoa pessoa = new Pessoa();
        pessoaEditController.setPessoa(pessoa);

        List<String> errorList = List.of("error_fill_name");
        
        pessoaEditController.save();

        verify(pessoaFacade).validateRequiredFields(pessoa, errorList);
        verify(logger).error(anyString(), any(Throwable.class));
        // Assert that a FacesMessage with error message is added to FacesContext
        verify(facesContext).addMessage(eq("msgsGlobal"), any(FacesMessage.class));
    }

    @Test
    public void testSelectedStateListener() throws BusinessException {
        Estado estado = new Estado();
        pessoaEditController.setSelectedEstado(estado);

        pessoaEditController.selectedStateListener();

        verify(pessoaFacade).listCidades(estado);
    }
    
    @Test
    public void testValidateRequiredFields_WithBlankNome() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(""); // Deixando o nome em branco

        pessoaFacade.validateRequiredFields(pessoa, errorList);

        assertTrue(errorList.contains("error_fill_name"));
        assertEquals(1, errorList.size());
    }

    @Test
    public void testValidateRequiredFields_WithBlankCpf() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João"); // Nome preenchido
        pessoa.setCpf(""); // Deixando o CPF em branco

        pessoaFacade.validateRequiredFields(pessoa, errorList);

        assertTrue(errorList.contains("error_fill_cpf"));
        assertEquals(1, errorList.size());
    }

    @Test
    public void testValidateRequiredFields_WithBothFieldsBlank() {
        Pessoa pessoa = new Pessoa(); // Ambos nome e CPF em branco

        pessoaFacade.validateRequiredFields(pessoa, errorList);

        assertTrue(errorList.contains("error_fill_name"));
        assertTrue(errorList.contains("error_fill_cpf"));
        assertEquals(2, errorList.size());
    }

    @Test
    public void testValidateRequiredFields_WithNoErrors() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João");
        pessoa.setCpf("12345678900");

        pessoaFacade.validateRequiredFields(pessoa, errorList);

        assertTrue(errorList.isEmpty());
    }
}
