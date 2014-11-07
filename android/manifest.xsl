<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:android="http://schemas.android.com/apk/res/android">

	<xsl:param name="leadBoltPackage"></xsl:param>
	<xsl:param name="leadBoltSectionId"></xsl:param>
	<xsl:param name="leadBoltFireworksKey"></xsl:param>


	<xsl:template match="meta-data[@android:name='LEADBOLT_PACKAGE']">
		<meta-data android:name="LEADBOLT_PACKAGE" android:value="{$leadBoltPackage}"/>
	</xsl:template>

	<xsl:template match="meta-data[@android:name='LEADBOLT_SECTION_ID']">
		<meta-data android:name="LEADBOLT_SECTION_ID" android:value="{$leadBoltSectionId}"/>
	</xsl:template>

	<xsl:template match="meta-data[@android:name='LEADBOLT_FIREWORKS_KEY']">
		<meta-data android:name="LEADBOLT_FIREWORKS_KEY" android:value="{$leadBoltFireworksKey}"/>
	</xsl:template>
	<!--	<xsl:strip-space elements="*" />-->
	<xsl:output indent="yes" />

	<xsl:template match="comment()" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
