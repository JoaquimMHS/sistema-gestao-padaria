package org.padaria.service;

import org.padaria.model.IEntity;
import java.util.List;

public interface IEntityService<T extends IEntity> {
    void cadastrar(T entidade);
    T buscar(int codigo);
    List<T> listar();
    void atualizar(T entidade);
    boolean remover(int codigo);
}
