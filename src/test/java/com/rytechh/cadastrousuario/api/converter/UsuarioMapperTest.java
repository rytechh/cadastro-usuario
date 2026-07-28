package com.rytechh.cadastrousuario.api.converter;

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
public class UsuarioMapperTest {


    UsuarioMapper usuarioMapper;

    UsuarioEntity usuarioEntity;

    EnderecoEntity enderecoEntity;

    UsuarioResponseDTO usuarioResponseDTO;

    EnderecoResponseDTO enderecoResponseDTO;

    LocalDateTime dataHora;

    @BeforeEach
    public void setUp() {
        usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
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
        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Sabner", 12345L,
                "Centro", "Alemanha", "Munique", "2324342");
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(2121L, "rytechh",
                "rytechh21@gmail.com", "RG", enderecoResponseDTO);
    }

    @Test
    void deveConverterParaUsuarioResponseDTO() {

        UsuarioResponseDTO dto = (usuarioMapper.paraUsuarioResponseDTO(usuarioEntity));

        assertEquals(usuarioResponseDTO, dto);

    }
}
