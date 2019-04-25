/*

 */

package org.gmu.chess.repositories;

import ca.watier.echechess.communication.redis.interfaces.GameRepository;
import ca.watier.echechess.communication.redis.model.GenericGameHandlerWrapper;
import ca.watier.echechess.engine.engines.GenericGameHandler;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndependentGameRepositoryImpl implements GameRepository<GenericGameHandler> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IndependentGameRepositoryImpl.class);
    private final Map<String, GenericGameHandlerWrapper<GenericGameHandler>> games = new HashMap<>();

    @Override
    public void add(GenericGameHandlerWrapper<GenericGameHandler> genericGameHandlerWrapper) {
        addGame(genericGameHandlerWrapper.getId(), genericGameHandlerWrapper);
    }

    private void addGame(String id, GenericGameHandlerWrapper<GenericGameHandler> genericGameHandlerWrapper) {
        LOGGER.info("Added new game with id {}", genericGameHandlerWrapper.getId());
        games.put(id, genericGameHandlerWrapper);
    }

    @Override
    public void add(String id, GenericGameHandler genericGameHandler) {
        addGame(id, new GenericGameHandlerWrapper<>(genericGameHandler));
    }

    @Override
    public void delete(String id) {
        games.remove(id);
    }

    @Override
    public GenericGameHandlerWrapper<GenericGameHandler> get(String id) {
        return games.get(id);
    }

    @Override
    public List<GenericGameHandlerWrapper<GenericGameHandler>> getAll() {
        return new ArrayList<>(games.values());
    }
}
