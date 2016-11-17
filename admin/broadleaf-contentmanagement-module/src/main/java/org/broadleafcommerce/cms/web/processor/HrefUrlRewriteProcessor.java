/*
 * #%L
 * BroadleafCommerce CMS Module
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */

package org.broadleafcommerce.cms.web.processor;

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.cms.file.service.StaticAssetPathService;
import org.broadleafcommerce.common.web.condition.TemplatingExistCondition;
import org.broadleafcommerce.common.web.domain.BroadleafAttributeModifier;
import org.broadleafcommerce.common.web.domain.BroadleafTemplateContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Similar to {@link UrlRewriteProcessor} but handles href tags.   
 * Mainly those that have a useCdn=true attribute or those that are inside a script tag.
 * 
 * @author bpolster
 */
@Component("blHrefUrlRewriteProcessor")
@Conditional(TemplatingExistCondition.class)
public class HrefUrlRewriteProcessor extends UrlRewriteProcessor {

    @Resource(name = "blStaticAssetPathService")
    protected StaticAssetPathService staticAssetPathService;

    private static final String LINK = "link";
    private static final String HREF = "href";

    @Override
    public String getName() {
        return HREF;
    }

    @Override
    public BroadleafAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, BroadleafTemplateContext context) {
        String useCDN = tagAttributes.get("useCDN");
        String hrefValue = attributeValue;
        if (LINK.equals(tagName) || StringUtils.equals("true", useCDN)) {
            hrefValue = super.getFullAssetPath(attributeValue, context);
        } else {
            hrefValue = super.parsePath(attributeValue, context);
        }
        Map<String, String> newAttributes = new HashMap<>();
        newAttributes.put(HREF, hrefValue);
        return new BroadleafAttributeModifier(newAttributes);
    }

}
