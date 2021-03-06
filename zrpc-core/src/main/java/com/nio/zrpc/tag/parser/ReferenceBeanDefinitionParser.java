package com.nio.zrpc.tag.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.nio.zrpc.tag.definition.ReferenceDefinition;



public class ReferenceBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ReferenceDefinition.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
	
		String interfaceName = element.getAttribute("interfaceName");
		String id = element.getAttribute("id");
		String loadbalance = element.getAttribute("loadbalance");

		if (StringUtils.hasText(interfaceName)) {
			bean.addPropertyValue("interfaceName", interfaceName);
		}
		if (StringUtils.hasText(loadbalance)) {
			bean.addPropertyValue("strategy", loadbalance);
		}else{
			bean.addPropertyValue("strategy", "hash");
		}
	
		if (StringUtils.hasText(id)) {
			bean.addPropertyValue("id", id);
		}

		
	}

}
