package hornet.framework.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.util.Optionals;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class CrudProjectionRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
implements CrudProjectionRepository<T, ID> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;

    private @Nullable CrudMethodMetadata metadata;

    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    public CrudProjectionRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
        this.entityInformation = entityInformation;
    }

    public CrudProjectionRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    @Override
    public <R> Optional<R> getByIdWithProjection(final ID id, final Class<R> projectionClass, final JpaEntityGraph dynamicEntityGraph) {
        Assert.notNull(id, "The given id must not be null!");
        final Map<String, Object> hints = this.getFetchGraphs(dynamicEntityGraph);

        final Class<T> domainType = getDomainClass();

        // Entité lu en base
        T source = null;

        if (metadata == null) {
            source = em.find(domainType, id, hints);
        } else {
            final LockModeType type = metadata.getLockModeType();

            source = type == null ? em.find(domainType, id, hints) : em.find(domainType, id, type, hints);
        }
        if (source == null) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(projectionFactory.createProjection(projectionClass, source));


    }

    @Override
    public <R> Optional<R> getByIdWithProjection(final ID id, final Class<R> projectionClass, final EntityGraph<T> dynamicEntityGraph) {
        Assert.notNull(id, "The given id must not be null!");

        final Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("javax.persistence.fetchgraph", dynamicEntityGraph); // fetchgraph ou loadgraph

        final Class<T> domainType = getDomainClass();

        if (metadata == null) {
            return Optional
                        .ofNullable(projectionFactory.createProjection(projectionClass, em.find(domainType, id, hints)));
        }

        final LockModeType type = metadata.getLockModeType();

        return Optional.ofNullable(projectionFactory.createProjection(projectionClass,
            type == null ? em.find(domainType, id, hints) : em.find(domainType, id, type, hints)));
    }

    private Map<String, Object> getFetchGraphs(final JpaEntityGraph dynamicEntityGraph) {

        if (dynamicEntityGraph != null) {
            return Jpa21Utils.tryGetFetchGraphHints(em, dynamicEntityGraph, entityInformation.getJavaType());
        }

        return (Map<String, Object>) Optionals
                    .mapIfAllPresent(Optional.ofNullable(em), metadata.getEntityGraph(), new BiFunction<EntityManager, org.springframework.data.jpa.repository.EntityGraph, Object>() {
                        @Override
                        public Object apply(EntityManager em,
                                org.springframework.data.jpa.repository.EntityGraph graph) {
                            return Jpa21Utils
                                .tryGetFetchGraphHints(em, getEntityGraph(graph), entityInformation.getJavaType());
                        }
                    })
                    .orElse(Collections.emptyMap());
    }

    private JpaEntityGraph getEntityGraph(final org.springframework.data.jpa.repository.EntityGraph entityGraph) {

        final String fallbackName = entityInformation.getEntityName() + "." + metadata.getMethod().getName();
        return new JpaEntityGraph(entityGraph, fallbackName);
    }

    @Override
    public EntityGraph<T> createEntityGraph() {
        return this.em.createEntityGraph(entityInformation.getJavaType());

    }

    @Override
    public EntityGraph<T> createEntityGraph(final String subGraphAttributName, final String... attributs) {
        final EntityGraph<T> graph = this.em.createEntityGraph(entityInformation.getJavaType());
        if (attributs != null) {
            graph.addAttributeNodes(attributs);
        }
        if (subGraphAttributName != null) {
            graph.addSubgraph(subGraphAttributName);
        }

        return graph;
    }

    @Override
    public <R> List<R> findAllWithProjection(final Class<R> projectionClass,
                @Nullable final Specification<T> spec,
                final Sort sort) {

        LOG.debug("find all avec projection {}", projectionClass.getClass());

        final List<T> result = super.findAll(spec, sort);
        final List<R> resultProjection = new ArrayList<R>();

        for (final T t : result) {
            resultProjection.add(projectionFactory.createProjection(projectionClass, t));
        }

        return resultProjection;
    }

    @Override
    public <R> List<R> findAllWithProjection(final Class<R> projectionClass,
                @Nullable final Specification<T> spec) {

        LOG.debug("find all avec projection {}", projectionClass.getClass());

        final List<T> result = super.findAll(spec);
        final List<R> resultProjection = new ArrayList<R>();

        for (final T t : result) {
            resultProjection.add(projectionFactory.createProjection(projectionClass, t));
        }

        return resultProjection;
    }
}