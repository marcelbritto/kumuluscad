package br.com.marcelbritto.kumuluscad.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.marcelbritto.kumuluscad.dao.PessoaDAO;
import br.com.marcelbritto.kumuluscad.model.Pessoa;

class PessoaFacadeTest {

	@Mock
    private PessoaDAO pessoaDAO;

    @InjectMocks
    private PessoaFacade pessoaFacade;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1);
        pessoa.setNome("João");

        when(pessoaDAO.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa created = pessoaFacade.create(pessoa);

        assertNotNull(created);
        assertEquals(1, created.getId());
        assertEquals("João", created.getNome());
    }

    @Test
    public void testListByNome() {
        String nome = "Maria";

        List<Pessoa> pessoas = new ArrayList<>();
        pessoas.add(new Pessoa(1, "Maria Silva", "12345678900"));
        pessoas.add(new Pessoa(2, "Maria Santos", "98765432100"));

        when(pessoaDAO.getObjects("Pessoa.findByNomeLike", new String[]{"nome"}, new String[]{"%Maria%"}))
                .thenReturn(pessoas);

        List<Pessoa> result = pessoaFacade.listByNome(nome);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testValidateRequiredFields() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("");
        pessoa.setCpf("");

        List<String> errorList = new ArrayList<>();
        pessoaFacade.validateRequiredFields(pessoa, errorList);

        assertTrue(errorList.contains("error_fill_name"));
        assertTrue(errorList.contains("error_fill_cpf"));
    }

}
