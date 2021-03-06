<?xml version="1.0"?>
<recipe>
    <#include "recipe_manifest.xml.ftl" />
    <#include "recipe_simple.xml.ftl" />
    <#include "recipe_list.xml.ftl" />
    <#include "recipe_app_bar.xml.ftl" />

    <instantiate from="src/app_package/classes/Activity.kotlin.ftl"
      to="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}.kt" />

    <instantiate from="src/app_package/classes/Adapter.kotlin.ftl"
      to="${escapeXmlAttribute(srcOut)}/view/adapter/${AdapterClass}.kt" />

    <instantiate from="src/app_package/classes/Presenter.kotlin.ftl"
      to="${escapeXmlAttribute(srcOut)}/presenter/${presenterClass}.kt" />

    <instantiate from="src/app_package/classes/Bean.kotlin.ftl"
      to="${escapeXmlAttribute(rootOut)}/${slashedPackageName(BeanPackageName)}/${BeanClass}.kt"/>

    <instantiate from="src/app_package/classes/Param.kotlin.ftl"
      to="${escapeXmlAttribute(rootOut)}/${slashedPackageName(ParamPackageName)}/${ParamClass}.kt"/>

    <instantiate from="dimens.xml.ftl"
      to="${escapeXmlAttribute(resOut)}/values/dimens.xml" />

    <open file="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}.kt"/>
    <open file="${escapeXmlAttribute(srcOut)}/view/adapter/${AdapterClass}.kt"/>
</recipe>
