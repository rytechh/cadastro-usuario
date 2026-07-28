package com.rytechh.cadastrousuario.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rytechh.cadastrousuario.api.request.EnderecoRequestDTO;
import com.rytechh.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.rytechh.cadastrousuario.api.request.UsuarioRequestDTO;
import com.rytechh.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.rytechh.cadastrousuario.api.response.UsuarioResponseDTO;
import com.rytechh.cadastrousuario.business.UsuarioService;
import com.rytechh.cadastrousuario.infrastructure.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @InjectMocks
    UsuarioController usuarioController;

    @Mock
    UsuarioService usuarioService;

    MockMvc mockMvc;

    String url;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoRequestDTO enderecoRequestDTO;

    UsuarioResponseDTO usuarioResponseDTO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    String json;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).
                alwaysDo(print())
                .build();
        url = "/user";
        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Sabner", 12345L,
                "Centro", "Alemanha", "Munique", "2324342");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("rytechh",
                "rytechh21@gmail.com", "RG", enderecoRequestDTO);
        json = objectMapper.writeValueAsString(usuarioRequestDTO);

    }


    @Test
    void deveGravarUsuarioComSucesso() throws Exception {
        when(usuarioService.gravarUsuarios(any())).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(usuarioService).gravarUsuarios(any());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void naoDeveGravarDadosUsuarioCasoJsonSejaNullo() throws Exception {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(usuarioService);
    }


    @Test
    void deveAtualizarDadosUsuarioComSucesso() throws Exception {
        when(usuarioService.atualizaCadastro(any())).thenReturn(usuarioResponseDTO);

        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(usuarioService).atualizaCadastro(any());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void naoDeveAtualizarCadastroCasoJsonNullo() throws Exception {
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        ).andExpect(status().isBadRequest());


        verifyNoInteractions(usuarioService);
    }


    @Test
    void deveBuscarUsuarioPorEmailComSucesso() throws Exception {
        when(usuarioService.buscaDadosUsuario(usuarioRequestDTO.getEmail()))
                .thenReturn(usuarioResponseDTO);

        mockMvc.perform(get(url)
                        .param("email", usuarioRequestDTO.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(usuarioService).buscaDadosUsuario(usuarioRequestDTO.getEmail());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveRetornarVazioQuandoEmailNaoForEncontrado() throws Exception {
        when(usuarioService.buscaDadosUsuario(anyString())).thenReturn(null);

        mockMvc.perform(get(url)
                        .param("email", "naoexiste@email.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(usuarioService).buscaDadosUsuario("naoexiste@email.com");
    }

    @Test
    void deveDeletarDadosUsuarioComSucesso() throws Exception {
        doNothing().when(usuarioService).deletaDadosUsuario(usuarioRequestDTO.getEmail());

        mockMvc.perform(delete(url)
                        .param("email", usuarioRequestDTO.getEmail()))
                .andExpect(status().isAccepted());

        verify(usuarioService).deletaDadosUsuario(usuarioRequestDTO.getEmail());
        verifyNoMoreInteractions(usuarioService);
    }
}



