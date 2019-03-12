package com.goomo.drlfilegenerator.service;

import java.util.ArrayList;
import java.util.Collection;

import org.drools.compiler.lang.DrlDumper;
import org.drools.compiler.lang.descr.AndDescr;
import org.drools.compiler.lang.descr.ExprConstraintDescr;
import org.drools.compiler.lang.descr.ImportDescr;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.lang.descr.PatternDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

public class DrlLogic {
	PackageDescr packageDescription;
	
	private void SetupPackage() {
		packageDescription.setName("com.goomo.drlfilegenerator;");
		
		Collection<ImportDescr> collectionImportDescr = new ArrayList<ImportDescr>();
		ImportDescr importDescr1 = new ImportDescr("java.util.ArrayList;");
		ImportDescr importDescr2 = new ImportDescr("java.util.List;");
		ImportDescr importDescr3 = new ImportDescr("com.goomo.drlfilegenerator.domain.Hotel;");
		ImportDescr importDescr4 = new ImportDescr("com.goomo.drlfilegenerator.domain.HotelBooking;");
		collectionImportDescr.add(importDescr1);
		collectionImportDescr.add(importDescr2);
		collectionImportDescr.add(importDescr3);
		collectionImportDescr.add(importDescr4);
		packageDescription.addAllImports(collectionImportDescr);
	}
	
	public String generate() {
		packageDescription = new PackageDescr();
		SetupPackage();
		RuleDescr ruleDescription = new RuleDescr();
		ruleDescription.setName("20% discount on all seected hotels");
		AndDescr finalLHS = CreatePatternLHS();
		ruleDescription.setLhs(finalLHS);
		ruleDescription.setConsequence(createConsequence());
		packageDescription.addRule(ruleDescription);
		String drlString = new DrlDumper().dump(packageDescription);
		System.out.println("Rule-------->>>>>>" + "\n" + drlString);
		
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		kbuilder.add(ResourceFactory.newByteArrayResource(drlString.getBytes()), ResourceType.DRL);

		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			StringBuilder errorString = new StringBuilder();
			errorString.append("Could not parse knowledge.");
			for (KnowledgeBuilderError error : errors) {
				errorString.append("Error Message :  " + error.getMessage() + ".\n");
			}
			System.out.println("Rule has validation errors: " + errorString);
		} 
		return drlString;
	}

	private String createConsequence() {
		StringBuilder consequenceBuilder = new StringBuilder();
		consequenceBuilder.append("\t");
		consequenceBuilder.append("$hotel.setPrice(($hotel.getPrice() * 80) / 100);");
		consequenceBuilder.append(System.lineSeparator());
		consequenceBuilder.append("System.out.println(\"Hotel price after discount  is : \" + $hotel.getPrice());");
		return consequenceBuilder.toString();
	}

	private AndDescr CreatePatternLHS() {
		AndDescr finalAnd = new AndDescr();
		
		PatternDescr pattrnDescr1 = new PatternDescr();
		pattrnDescr1.setObjectType("HotelBooking");
		pattrnDescr1.setIdentifier("$hotelsList");
		
		PatternDescr pattrnDescr2 = new PatternDescr();
		pattrnDescr2.setIdentifier("$hotel");
		pattrnDescr2.setObjectType("");
		String expresssionString = "Hotel() from $hotelsList.getListOfHotels()";
		ExprConstraintDescr exprConstraintDescr = new ExprConstraintDescr();
		exprConstraintDescr.setExpression(expresssionString.toString());
		pattrnDescr2.addConstraint(exprConstraintDescr);
		finalAnd.addOrMerge(pattrnDescr1);
		finalAnd.addOrMerge(pattrnDescr2);
		return finalAnd;
	}
}
