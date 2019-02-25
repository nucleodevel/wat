package org.nucleodevel.webapptemplate.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.nucleodevel.webapptemplate.entity.AbstractEntity;
import org.nucleodevel.webapptemplate.util.ParameterizedClassUtils;

/**
 * <p>
 *   Classe abstrata que implementa o comportamento padrão de um DAO que provê acesso ao modelo de 
 *   dados nos sistemas que usam webapptemplate. Ela provê acesso a um datasource, que pode um SGBD
 *   ou um WebService, e repassa a esses as operações de persistência necessárias.
 * </p>
 * @author Dallan Augusto Toledo Reis
 * @param <E> subclasse de AbstractEntity que mapeia uma entidade em um datasource.
 */
public abstract class AbstractDao<E extends AbstractEntity<?>> {
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Atributos
	 * --------------------------------------------------------------------------------------------
	 */
	
    
	/**
     * <p>
     *   Atributo que armazena a classe assumida por E, que é a entidade alvo do DAO. Geralmente 
     *   usado para se obter nome desta classe.
     * </p>
     */
    private Class<E> entityClass;
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Getters and setters
	 * --------------------------------------------------------------------------------------------
	 */
	

    /**
     * <p>
     *   Obtém tipo class do tipo E via ParameterizedClassUtils
     * </p>
     */
    @SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
    	if (entityClass == null)
    		entityClass = 
    			(Class<E>) ParameterizedClassUtils.
    				getParameterClassFromParameterizedClass(getClass(), 0);
    	return entityClass;
    }

	/**
	 * <p>
	 *   Método que retorna uma nova instância do tipo parametrizado E via construtor default. Ou 
	 *   seja, toda classe que for assumida por E deve ter um construtor padrão sem parâmetros.
	 * </p>
	 * @return Instância da classe parametrizada E
	 */
	@SuppressWarnings("unchecked")
	public E getNewEntityInstance() {
		try {
			Constructor<?> cons = getEntityClass().getConstructor();   
			return (E) cons.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
	 * <p>
	 *   Método usado antes das operações de inserção e atualização no datasource para evitar que 
	 *   uma entidade não única seja persistida e cause algum erro. A indicação isInsert é 
	 *   necessária porque se é desejado uma edição, a busca pode retornar a própria entidade se 
	 *   não houve alteração dos parâmetros unique originais. Assim, é possível indicar que esta 
	 *   busca pode ser ignorada.
	 * </p>
	 * @param entity Entidade E cuja unicidade será testada.
	 * @param isInsert Indica se a entidade será inserida ou editada no datasource. Essa informação 
	 *   é importante para verificar a unicidade da entidade.
	 * @return Booleano que retorna o resultado da verificação da unicidade de selected.
	 */
	public boolean isAnUniqueEntity(E entity, boolean isInsert) {
		Map<String, Object> params = getUniqueParams(entity);
		if (params != null) {
			List<E> uniqueItems = selectAllByUniqueParams(getUniqueParams(entity));
			if (uniqueItems != null)
				for (E x: uniqueItems)
					if (isInsert || !entity.equals(x))
						return false;
		}
		return true;
	}
    
    /**
     * <p>
     *   É delegado às subclasses o trabalho de designar quais atributos de selected devem ser 
     *   testados a fim de saber se ele é unique.
     * </p>
     * @param entity Entidade E da qual será formado o mapeamento dos parâmetros unique.
     * @return Mapa de parâmetros unique de selected.
     */
    protected abstract Map<String, Object> getUniqueParams(E entity);
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Operações de leitura de dados no datasource 
	 * --------------------------------------------------------------------------------------------
	 */


	/**
	 * <p>
	 *   Cada subclasse deve implementar um método que obtém todas as entidades E do datasource no 
	 *   momento. Como esta operação é dependente do tipo do datasource, ela é delegada às 
	 *   subclasses.
	 * </p>
	 * @return Lista com todas as entidades E presentes no datasource.
	 */
	public abstract List<E> selectAll();
	
	/**
	 * <p>
	 *   Obtém uma ou mais entidades E que correspondem aos parâmetros passados para testar a 
	 *   unicidade destes. 
	 * </p>
	 * @param params parâmetros cuja unicidade será testada.
	 * @return Uma ou mais entidades E que correspondem aos parâmetros passados.
	 */
	public abstract List<E> selectAllByUniqueParams(Map<String, Object> params);
	
	/**
	 * <p>
	 *   Cada subclasse deve implementar um método que obtém todas as entidades E do datasource que 
	 *   estejam dentro do intervalo passado por parâmetro.
	 * </p>
	 * @param Intervalo de leitura de entidades E no datasource.
	 * @return Lista com todas as entidades E presentes no datasource que estejam dentro do 
	 *   intervalo passado por parâmetro.
	 */
	public abstract List<E> selectAllByRange(int[] range);
    
    /**
     * <p>
     *   Retorna o número de entidades E existentes no datasource.
     * </p>
     * @return Número total de entidades E existentes  
     */
    public int selectCount() {
    	return selectAll().size();
    }

	/**
	 * <p>
	 *   Obtém a entidade E cujo ID é aquele passado por parâmetro.
	 * </p>
	 * @param id ID da instância que será fornecida pelo datasource.
	 * @return Instância cujo ID foi passado por parâmetro ou nulo, se não existir instância com 
	 *   tal ID.
	 */
	public abstract E selectOne(Object id);
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Operações de escrita de dados no datasource 
	 * --------------------------------------------------------------------------------------------
	 */
	

	/**
	 * <p>
	 *   Método que realiza a inserção da entidade no datasource.
	 * </p>
	 * @param entity Entidade que será inserida no datasource.
	 * @return Mesma instância persistida, eventualmente com o novo ID.
	 */
	public abstract E insert(E entity);

	/**
	 * <p>
	 *   Método que realiza a atualização da entidade no datasource.
	 * </p>
	 * @param entity Entidade que será inserida no datasource.
	 * @return Mesma instância persistida.
	 */
	public abstract E update(E entity);

	/**
	 * <p>
	 *   Método que realiza a remoção da entidade no datasource.
	 * </p>
	 * @param entity Entidade que será inserida no datasource.
	 * @return Mesma instância persistidaD.
	 */
	public abstract E delete(E entity);
	
	
	/* 
	 * --------------------------------------------------------------------------------------------
	 *   Utils
	 * --------------------------------------------------------------------------------------------
	 */
	
	
	/**
	 * <p>
	 *   Método usado por outros que não possuem uma estratégia própria de ordenação de listas de 
	 *   entidades do tipo E.
	 * </p>
	 * @param entities Lista que será ordenada.
	 * @return Mesma lista ordenada.
	 */
	public List<E> sort(List<E> entities) {
		Collections.sort(entities);
		return entities;
	}
    
}