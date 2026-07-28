package com.rytechh.cadastrousuario.business;

import com.rytechh.cadastrousuario.api.converter.UsuarioConverter;
import com.rytechh.cadastrousuario.api.converter.UsuarioMapper;
import com.rytechh.cadastrousuario.api.converter.UsuarioUpdateMapper;
import com.rytechh.cadastrousuario.api.request.EnderecoRequestDTO;
import com.rytechh.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.rytechh.cadastrousuario.api.request.UsuarioRequestDTO;
import com.rytechh.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.rytechh.cadastrousuario.api.response.EnderecoResponseDTO;
import com.rytechh.cadastrousuario.api.response.EnderecoResponseDTOFixture;
import com.rytechh.cadastrousuario.api.response.UsuarioResponseDTO;
import com.rytechh.cadastrousuario.api.response.UsuarioResponseDTOFixture;
import com.rytechh.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.rytechh.cadastrousuario.infrastructure.entities.UsuarioEntity;
import com.rytechh.cadastrousuario.infrastructure.exceptions.BusinessException;
import com.rytechh.cadastrousuario.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {


    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    UsuarioConverter usuarioConverter;

    @Mock
    UsuarioUpdateMapper usuarioUpdateMapper;

    @Mock
    UsuarioMapper usuarioMapper;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoRequestDTO enderecoRequestDTO;

    UsuarioResponseDTO usuarioResponseDTO;

    EnderecoResponseDTO enderecoResponseDTO;

    UsuarioEntity usuarioEntity;

    EnderecoEntity enderecoEntity;

    LocalDateTime dataHora;


    @BeforeEach
    public void setUp() {
        dataHora = LocalDateTime.of(2026, 7, 26, 20, 0, 0);
        enderecoEntity = EnderecoEntity.builder()
                .rua("Sabner")
                .numero(12345L)
                .bairro("Centro")
                .cep("2324342")
                .cidade("Munique")
                .complemento("Alemanha")
                .build();
        usuarioEntity = UsuarioEntity.builder()
                .nome("rytechh")
                .email("rytechh21@gmail.com")
                .documento("RG")
                .dataCadastro(dataHora)
                .endereco(enderecoEntity)
                .build();
        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Sabner", 12345L,
                "Centro", "Alemanha", "Munique", "2324342");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("rytechh",
                "rytechh21@gmail.com", "RG", enderecoRequestDTO);

        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Sabner", 12345L,
                "Centro", "Alemanha", "Munique", "2324342");
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(2121L, "rytechh",
                "rytechh21@gmail.com", "RG", enderecoResponseDTO);

    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);

        UsuarioEntity entity = usuarioService.salvaUsuario(usuarioEntity);

        assertEquals(usuarioEntity, entity);

        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository);
    }


    @Test
    void deveGravarUsuarioComSucesso() {
        when(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.gravarUsuarios(usuarioRequestDTO);

        assertEquals(usuarioResponseDTO, resultado);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
    }

    @Test
    void deveLancarExcecaoQuandoJsonForNulo() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> usuarioService.gravarUsuarios(null));

        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(exception.getCause(), notNullValue());
        assertThat(exception.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));

        verifyNoInteractions(usuarioRepository, usuarioConverter, usuarioMapper);
    }

    @Test
    void deveLancarExcecaoQuandoConverterFalhar() {
        when(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO))
                .thenThrow(new RuntimeException("falha inesperada na conversão"));

        assertThrows(BusinessException.class,
                () -> usuarioService.gravarUsuarios(usuarioRequestDTO));

        verify(usuarioRepository, never()).saveAndFlush(any());
    }


    @Test
    void deveAtualizarCadastroComSucesso() {
        when(usuarioRepository.findByEmail(usuarioRequestDTO.getEmail())).thenReturn(usuarioEntity);
        when(usuarioUpdateMapper.updateUsuarioFromDTO(usuarioRequestDTO, usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.atualizaCadastro(usuarioRequestDTO);

        assertEquals(usuarioResponseDTO, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoRequestForNulo() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> usuarioService.atualizaCadastro(null));

        assertEquals("Erro ao gravar dados de usuário", exception.getMessage());
        verifyNoInteractions(usuarioRepository, usuarioUpdateMapper, usuarioMapper);
    }

    @Test
    void deveLancarBusinessExceptionQuandoUsuarioNaoForEncontrado() {
        when(usuarioRepository.findByEmail(usuarioRequestDTO.getEmail())).thenReturn(null);
        when(usuarioUpdateMapper.updateUsuarioFromDTO(usuarioRequestDTO, null))
                .thenThrow(new NullPointerException());

        assertThrows(BusinessException.class,
                () -> usuarioService.atualizaCadastro(usuarioRequestDTO));

        verify(usuarioRepository, never()).saveAndFlush(any());
    }


    @Test
    void deveRetornarUsuarioQuandoEmailExistir() {
        when(usuarioRepository.findByEmail(usuarioEntity.getEmail())).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.buscaDadosUsuario(usuarioEntity.getEmail());

        assertEquals(usuarioResponseDTO, resultado);
    }

    @Test
    void deveRetornarNuloQuandoEmailNaoExistir() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(null);

        UsuarioResponseDTO resultado = usuarioService.buscaDadosUsuario("naoexiste@email.com");

        assertNull(resultado);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    void deveDeletarDadosDeUsuario() {
        usuarioRepository.deleteByEmail("rytechh21@gmail.com");

        verify(usuarioRepository).deleteByEmail("rytechh21@gmail.com");
        verifyNoMoreInteractions(usuarioRepository);
    }
}

