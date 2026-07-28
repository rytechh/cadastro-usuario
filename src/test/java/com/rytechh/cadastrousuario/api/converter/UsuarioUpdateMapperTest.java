package com.rytechh.cadastrousuario.api.converter;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UsuarioUpdateMapperTest {


    UsuarioUpdateMapper usuarioUpdateMapper;

    UsuarioEntity usuarioEntity;

    UsuarioEntity usuarioEntityEsperado;

    EnderecoEntity enderecoEntity;

    EnderecoEntity enderecoEntityEsperado;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoRequestDTO enderecoRequestDTO;

    LocalDateTime dataHora;

    @BeforeEach
    public void setUp() {
        usuarioUpdateMapper = Mappers.getMapper(UsuarioUpdateMapper.class);
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
                .id(2121L)
                .endereco(enderecoEntity)
                .build();
        enderecoRequestDTO = EnderecoRequestDTOFixture.build("MarBela", 300L,
                null, "Espanha", "Málaga", "19085000");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build( "Raian",
                null, "2167", enderecoRequestDTO);

        enderecoEntityEsperado = EnderecoEntity.builder()
                .rua("MarBela")
                .numero(300L)
                .bairro("Centro")
                .complemento("Espanha")
                .cidade("Málaga")
                .cep("19085000")
                .build();

        usuarioEntityEsperado = UsuarioEntity.builder()
                .id(2121L)
                .nome("Raian")
                .email("rytechh21@gmail.com")
                .documento("2167")
                .dataCadastro(dataHora)
                .endereco(enderecoEntity)
                .build();
    }

    @Test
    void deveAtualizarUsuarioEntityComDadosVindoDTO() {

        UsuarioEntity usuario = usuarioUpdateMapper.updateUsuarioFromDTO(usuarioRequestDTO, usuarioEntity);

        assertEquals(usuarioEntityEsperado, usuario);

    }

    @Test
    void deveAtualizarEnderecoEntityComDadosVindoDTO() {

        EnderecoEntity endereco = usuarioUpdateMapper.updateEnderecoFromDTO(enderecoRequestDTO, enderecoEntity);

        assertEquals(enderecoEntityEsperado, endereco);
    }
}
