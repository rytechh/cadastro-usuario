package com.rytechh.cadastrousuario.api.converter;

import com.rytechh.cadastrousuario.api.request.EnderecoRequestDTO;
import com.rytechh.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.rytechh.cadastrousuario.api.request.UsuarioRequestDTO;
import com.rytechh.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.rytechh.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.rytechh.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UsuarioConverterTest {

    @InjectMocks
    UsuarioConverter usuarioConverter;

    @Mock
    Clock clock;

    UsuarioEntity usuarioEntity;

    EnderecoEntity enderecoEntity;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoRequestDTO enderecoRequestDTO;

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

        ZoneId zoneId = ZoneId.systemDefault();
        Clock fixedClock = Clock.fixed(dataHora.atZone(zoneId).toInstant(), zoneId);
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();


//        enderecoResponseDTO = EnderecoResponseDTOFixture.build(1234567L, "Teste Unitário", "12345",
//                "Complemento", "São Paulo", "SP", "44340000");
//        usuarioResponseDTO = UsuarioResponseDTOFixture.build("rytechh", "rytechh21@gmail.com", "1234");
    }

    @Test
    void deveConverterParaUsuarioEntity() {

        UsuarioEntity entity = (usuarioConverter.paraUsuarioEntity(usuarioRequestDTO));

        assertEquals(usuarioEntity, entity);

    }
}
