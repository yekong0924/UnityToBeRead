package com.weavernorth.datadeal.toread.manage.mongodb.dao;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.weavernorth.datadeal.toread.manage.mongodb.pojo.MongoToReadDataObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoToReadDataDao extends MongoBaseDao<MongoToReadDataObject> {
	@Override
	protected Class<MongoToReadDataObject> getEntityClass() {
		return MongoToReadDataObject.class;
	}

}
