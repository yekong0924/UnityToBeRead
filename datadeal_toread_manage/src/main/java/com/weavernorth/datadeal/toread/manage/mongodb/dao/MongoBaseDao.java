package com.weavernorth.datadeal.toread.manage.mongodb.dao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.weavernorth.datadeal.toread.manage.mongodb.pojo.MongoToReadDataObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class MongoBaseDao<T> {

	protected static final Integer PAGE_SIZE = 10;
	/**
	 * spring mongodb 集成操作类，使用MongoTemplate作为工具操作mongodb
	 */
	@Autowired
	protected MongoTemplate mongoTemplate;
	/**
	MongoTemplate的常用方法;
	mongoTemplate.findAll(Student.class): 查询Student文档的全部数据
	mongoTemplate.findById(<id>, Student.class): 查询Student文档id为id的数据
	mongoTemplate.find(query, Student.class);: 根据query内的查询条件查询
	mongoTemplate.upsert(query, update, Student.class): 修改
	mongoTemplate.remove(query, Student.class): 删除
	mongoTemplate.insert(student): 新增
	 */




	/**
	 * 获取需要操作的实体类class
	 * 
	 * @return
	 */
	protected abstract Class<T> getEntityClass();

	/**
	 * 通过条件查询,查询分页结果
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @param collectionName collection名称
	 * @return
	 */
	public Pagination<T> getPage(int pageNo, int pageSize, Query query, String collectionName) {
		long totalCount = this.mongoTemplate.count(query, this.getEntityClass(), collectionName);
		Pagination<T> page = new Pagination<T>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);// 从skip开始,取多少条记录
		List<T> datas = this.find(query, collectionName);
		page.setDatas(datas);
		page.setTotalCount(totalCount);
		return page;
	}
	/**
	 * 通过条件查询指定collection的实体(集合)
	 *
	 * @param query
	 * @param collectionName collection名称
	 */
	public List<T> find(Query query, String collectionName) {
		return mongoTemplate.find(query, this.getEntityClass(), collectionName);
	}


	/**
	 * 功能描述: 创建索引
	 * 索引是顺序排列，且唯一的索引
	 *
	 * @param collectionName 集合名称，相当于关系型数据库中的表名
	 * @param filedName      对象中的某个属性名
	 * @return:java.lang.String
	 */
	public String createIndex(String collectionName, String filedName) {
		//配置索引选项
		IndexOptions options = new IndexOptions();
		// 设置为唯一
		options.unique(true);
		//创建按filedName升序排的索引
		return mongoTemplate.getCollection(collectionName).createIndex(Indexes.ascending(filedName), options);
	}


	/**
	 * 功能描述: 获取当前集合对应的所有索引的名称
	 *
	 * @param collectionName
	 * @return:java.util.List<java.lang.String>
	 */
	public List<String> getAllIndexes(String collectionName) {
		ListIndexesIterable<Document> list = mongoTemplate.getCollection(collectionName).listIndexes();
		//上面的list不能直接获取size，因此初始化arrayList就不设置初始化大小了
		List<String> indexes = new ArrayList<>();
		for (org.bson.Document document : list) {
			document.entrySet().forEach((key) -> {
				//提取出索引的名称
				if (key.getKey().equals("name")) {
					indexes.add(key.getValue().toString());
				}
			});
		}
		return indexes;
	}

	/**
	 * 功能描述: 往对应的集合中插入一条数据
	 *
	 * @param info           存储对象
	 * @param collectionName 集合名称
	 * @return:void
	 */
	public void insert(T info, String collectionName) {
		mongoTemplate.insert(info, collectionName);
	}
	/**
	 * 功能描述: 往对应的集合中插入一条数据，往对应的集合中插入一条数据 存在则更新 使用inset时，数据存在则报错
	 *
	 * @param info           存储对象
	 * @param collectionName 集合名称
	 * @return:void
	 */
	public void save(T info, String collectionName) {
		mongoTemplate.save(info, collectionName);
	}
	/**
	 * 功能描述: 往对应的集合中批量插入数据，注意批量的数据中不要包含重复的id
	 *
	 * @param infos 对象列表
	 * @return:void
	 */

	public void insertMulti(List<T> infos, String collectionName) {
		mongoTemplate.insert(infos, collectionName);
	}

	/**
	 * 功能描述: 使用索引信息精确更改某条数据
	 *
	 * @param id             唯一键
	 * @param collectionName 集合名称
	 * @param info           待更新的内容
	 * @return:void
	 */
	public void updateById(String id, String collectionName, T info) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();
		String str = JSON.toJSONString(info);
		JSONObject jQuery = JSON.parseObject(str);
		jQuery.forEach((key, value) -> {
			//因为id相当于传统数据库中的主键，这里使用时就不支持更新，所以需要剔除掉
			if (!key.equals("id")) {
				update.set(key, value);
			}
		});
		mongoTemplate.updateMulti(query, update, info.getClass(), collectionName);
	}

	/**
	 * 功能描述: 根据id删除集合中的内容
	 *
	 * @param id             序列id
	 * @param collectionName 集合名称
	 * @param clazz          集合中对象的类型
	 * @return:void
	 */

	public void deleteById(String id, Class<T> clazz, String collectionName) {
		// 设置查询条件，当id=#{id}
		Query query = new Query(Criteria.where("id").is(id));
		// mongodb在删除对象的时候会判断对象类型，如果你不传入对象类型，只传入了集合名称，它是找不到的
		// 上面我们为了方便管理和提升后续处理的性能，将一个集合限制了一个对象类型，所以需要自行管理一下对象类型
		// 在接口传入时需要同时传入对象类型
		mongoTemplate.remove(query, clazz, collectionName);
	}

	/**
	 * 功能描述: 根据id查询信息
	 *
	 * @param id             注解
	 * @param clazz          类型
	 * @param collectionName 集合名称
	 * @return:java.util.List<T>
	 */

	public T selectById(String id, Class<T> clazz, String collectionName) {
		// 查询对象的时候，不仅需要传入id这个唯一键，还需要传入对象的类型，以及集合的名称
		return mongoTemplate.findById(id, clazz, collectionName);
	}

	/**
	 * 功能描述: 查询列表信息
	 * 将集合中符合对象类型的数据全部查询出来
	 *
	 * @param collectName 集合名称
	 * @param clazz       类型
	 * @return:java.util.List<T>
	 */
	public List<T> selectList(String collectName, Class<T> clazz) {
		return selectList(collectName, clazz, null, null);
	}

	/**
	 * 功能描述: 分页查询列表信息
	 *
	 * @param collectName 集合名称
	 * @param clazz       对象类型
	 * @param currentPage 当前页码
	 * @param pageSize    分页大小
	 * @return:java.util.List<T>
	 */
	public List<T> selectList(String collectName, Class<T> clazz, Integer currentPage, Integer pageSize) {
		//设置分页参数
		Query query = new Query();
		//设置分页信息
		if (!ObjectUtils.isEmpty(currentPage) && ObjectUtils.isEmpty(pageSize)) {
			query.limit(pageSize);
			query.skip(pageSize * (currentPage - 1));
		}
		return mongoTemplate.find(query, clazz, collectName);
	}


	/**
	 *  功能描述: 根据条件查询集合
	 * @param collectName 集合名称
	 * @param query 查询条件
	 * @param clazz  对象类型
	 * @return
	 */
	public List<T> selectByQuery(String collectName,Query query,Class<T> clazz){
		List<T> result= mongoTemplate.find(query,clazz,collectName);
		return  result;
	}
	/**
	 *@描述 根据系统标识和flowid及接收人 判断数据是否存在，如果存在则更新数据
	 *@方法名称  findAndModify
	 *@参数  [reqFlowId, reqSysCode, reqReceiver, reqIsRemark, reqPcUrl, reqAppUrl]
	 *@返回值  com.weavernorth.datadeal.toread.manage.mongodb.pojo.MongoToReadDataObject
	 *@创建人  liupeng(1203153229@qq.com)
	 *@创建时间  2020/7/10
	 *@修改人和其它信息
	 */
	public  String findByQuery(String reqFlowId,String reqSysCode,String reqIsRemark,String reqReceiver,String reqDataType){
		//默认不存在
		String rspMongdbDataID="";
		MongoToReadDataObject mongoToReadDataObject=new MongoToReadDataObject();
		Query query = new Query();
		query.addCriteria(Criteria.where("flowId").is(reqFlowId));
		query.addCriteria(Criteria.where("sysCode").is(reqSysCode));
		query.addCriteria(Criteria.where("isRemark").is(reqIsRemark));
		query.addCriteria(Criteria.where("receiver").is(reqReceiver));
		mongoToReadDataObject=mongoTemplate.findOne(query, MongoToReadDataObject.class, reqDataType);
		if(null!=mongoToReadDataObject){
			rspMongdbDataID=mongoToReadDataObject.get_id();
		}
		return rspMongdbDataID;
	}
	/**
	 *@描述  根据mongdb数据id更新数据状态
	 *@方法名称  updateDataByMongoDBId
	 *@参数  [reqMongdbDataID, reqIsRemark, reqDataType]
	 *@返回值  void
	 *@创建人  liupeng(1203153229@qq.com)
	 *@创建时间  2020/7/10
	 *@修改人和其它信息

	 */
	public void updateDataByMongoDBId(String reqMongdbDataID,String  reqIsRemark,String reqDataType){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(reqMongdbDataID));
		Update update=new Update();
		update.set("isRemark",reqIsRemark);
		mongoTemplate.updateFirst(query,update,reqDataType);
	}
}
