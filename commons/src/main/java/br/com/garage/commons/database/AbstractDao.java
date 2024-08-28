package br.com.garage.commons.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.garage.commons.models.AggregateRoot;
import br.com.garage.commons.dtos.PaginationResult;
import br.com.garage.commons.exceptions.BusinessException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

/**
 * 
 * @author vinícius C. Rodrigues
 * @version 1.0
 * @param T descreve o tipo de classe de entidade relacional.
 *
 *          Classe abstrata que contem apenas um método publico, deve receber um
 *          Map com chave como um dos atributos da entidade e valor, um object,
 *          que será usado na cláusula WHERE do SQL. O Retorno da query é
 *          retornado dentro de uma instância do objeto PaginationResult.
 * 
 */

@Service
@Transactional
public class AbstractDao<T extends AggregateRoot> {

    private static final String UPDATED_AT = "atualizadoEm";

	private static final int INT_ONE = 1;

    @PersistenceContext
    private EntityManager em;

    /**
     * 
     * @param restrictions Map com o atributo da classe como chave e o valor que vai
     *                     comparar na cláusula WHERE da Query.
     * @param entity       entity a classe da entidade relacional.
     * @param page         inteiro que descreve em qual pagina está a paginação.
     * @param perPage      inteiro que representa o máximo de resultados que deve
     *                     retornar em uma busca.
     * @return uma instância do objeto PaginationResult com o numero da pagina,
     *         quantidade de itens por pagina, o total de registros encontrados e
     *         uma List com os objetos encontrados.
     * 
     *         Se não for encontrado registros dentro da cláusula WHERE, retonar uma
     *         lista vazia.
     * 
     * @throws BusinessException
     */

    // @Transactional
    public PaginationResult<T> findByRestrictions(Map<String, Object> restrictions, Class<T> entity, Integer page,
            Integer perPage) throws BusinessException {

        PaginationResult<T> paginationResult = new PaginationResult<T>();

        if (restrictions == null) {
            restrictions = new HashMap<String, Object>();
        }

        CriteriaQuery<T> criteriaQuery = returnCriteriaQuery(restrictions, entity);
        Long count = getTotalSize(restrictions, entity);
        paginationResult.setSize(count.intValue());

        if (page != null && perPage != null && perPage != 0) {
            page--;
            int firstResult = (page * perPage);
            List<T> resultList = em.createQuery(criteriaQuery).setFirstResult(firstResult).setMaxResults(perPage)
                    .getResultList();

            createPaginationResult(page, perPage, paginationResult, resultList);

        } else {
            List<T> resultList = em.createQuery(criteriaQuery).getResultList();

            paginationResult.setResult(resultList);
            paginationResult.setPage(INT_ONE);
            paginationResult.setPerPage(count.intValue());
        }

        return paginationResult;
    }

    private CriteriaQuery<T> returnCriteriaQuery(Map<String, Object> restrictions, Class<T> entity) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);
        Root<T> itemRoot = criteriaQuery.from(entity);
        List<Predicate> predicates = new ArrayList<Predicate>();

        try {
            validateFields(restrictions, entity);
        } catch (Exception e) {
            throw new BusinessException(e);
        }

        restrictions.forEach((key, value) -> {
            if (value instanceof Collection<?> == false) {
                predicates.add(criteriaBuilder.equal(itemRoot.get(key), value));
            }
        });

        criteriaQuery.select(itemRoot);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

        Order order = criteriaBuilder.desc(itemRoot.get(UPDATED_AT));
        criteriaQuery.orderBy(order);
        return criteriaQuery;
    }

    private void createPaginationResult(Integer page, Integer perPage, PaginationResult<T> pagination,
            List<T> resultListDto) {

        if (page == 0) {
            pagination.setFirstItem(INT_ONE);
        } else {
            pagination.setFirstItem(perPage * page + INT_ONE);
        }

        pagination.setLastItem(perPage * (page + INT_ONE));

        if (pagination.getLastItem() > pagination.getSize()) {
            pagination.setLastItem(pagination.getSize());
        }

        pagination.setLastPage((int) Math.ceil(pagination.getSize() / (double) perPage));
        pagination.setPage(page + 1);
        pagination.setPerPage(perPage);
        pagination.setResult(resultListDto);
    }

    private Long getTotalSize(Map<String, Object> restrictions, Class<T> entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> itemRoot = cq.from(entity);

        List<Predicate> predicates = new ArrayList<Predicate>();

        restrictions.forEach((key, value) -> {
            if (value instanceof Collection<?> == false) {
                predicates.add(cb.equal(itemRoot.get(key), value));
            }
        });

        cq.select(cb.count(itemRoot));
        cq.where(cb.and(predicates.toArray(new Predicate[] {})));
        return em.createQuery(cq).getSingleResult();

    }

    private void validateFields(Map<String, Object> restrictions, Class<T> entity) throws Exception {
        Set<String> keySet = restrictions.keySet();
        for (String key : keySet) {
            try {
                Field field = entity.getDeclaredField(key);
                Field declaredField = entity.getSuperclass().getDeclaredField(key);
                if (field == null || declaredField == null) {
                    throw new Exception();
                }
            } catch (NoSuchFieldException | SecurityException e) {
                e.fillInStackTrace();
            }
        }
    }

}
