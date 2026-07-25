package com.rytechh.cadastrousuario.business;

import com.rytechh.cadastrousuario.api.converter.UsuarioConverter;
import com.rytechh.cadastrousuario.api.converter.UsuarioMapper;
import com.rytechh.cadastrousuario.api.converter.UsuarioUpdateMapper;
import com.rytechh.cadastrousuario.api.request.UsuarioRequestDTO;
import com.rytechh.cadastrousuario.api.response.UsuarioResponseDTO;
import com.rytechh.cadastrousuario.infrastructure.entities.UsuarioEntity;
import com.rytechh.cadastrousuario.infrastructure.exceptions.BusinessException;
import com.rytechh.cadastrousuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final UsuarioUpdateMapper usuarioUpdateMapper;
    private final UsuarioMapper usuarioMapper;


    public UsuarioEntity salvaUsuario(UsuarioEntity usuarioEntity) {
        return usuarioRepository.saveAndFlush(usuarioEntity);
    }

    public UsuarioResponseDTO gravarUsuarios(UsuarioRequestDTO usuarioRequestDTO) {
        try {
            notNull(usuarioRequestDTO, "Os dados do usuário são obrigatórios");
            UsuarioEntity usuarioEntity = salvaUsuario(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO));
            return usuarioMapper.paraUsuarioResponseDTO(usuarioEntity);
        } catch (Exception e) {
            throw new BusinessException("Erro ao gravar dados de usuário", e);
        }
    }

    public UsuarioResponseDTO atualizaCadastro(UsuarioRequestDTO usuarioRequestDTO){
        try {
            notNull(usuarioRequestDTO, "Os dados do usuário são obrigatórios");
            UsuarioEntity usuario = usuarioRepository.findByEmail(usuarioRequestDTO.getEmail());
            UsuarioEntity entity = usuarioUpdateMapper.updateUsuarioFromDTO(usuarioRequestDTO, usuario);
            return usuarioMapper.paraUsuarioResponseDTO(salvaUsuario(entity));
        } catch (Exception e) {
            throw new BusinessException("Erro ao gravar dados de usuário", e);
        }
    }

    public UsuarioResponseDTO buscaDadosUsuario(String email) {
            UsuarioEntity entity = usuarioRepository.findByEmail(email);

            return entity != null ? usuarioMapper.paraUsuarioResponseDTO(entity) : null;
    }

    public void deletaDadosUsuario(String email) {

        usuarioRepository.deleteByEmail(email);

    }


}
