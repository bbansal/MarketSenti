package com.marketsenti.Article;

import com.marketsenti.domain.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Article {
private String source;
private long timestamp;
private String text;
private List<CompanyInfo> companyList;

public Article(){
	super();
	companyList = new ArrayList<CompanyInfo>();
}

public Article(String source, long timestamp, String text, List<CompanyInfo> companyList) {
	super();
	this.source = source;
	this.timestamp = timestamp;
	this.text = text;
	this.companyList = companyList;
}

public void addCompany(CompanyInfo company){
	companyList.add(company);
}

public void removeCompany(CompanyInfo company){
	companyList.remove(company);
}

public void printCompanyList(){
	Iterator<CompanyInfo> iteratorCompanyList = companyList.iterator();
	while(iteratorCompanyList.hasNext()){
		System.out.println(iteratorCompanyList.next().getName());
	}
}

public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public long getTimestamp() {
	return timestamp;
}
public void setTimestamp(long timestamp) {
	this.timestamp = timestamp;
}

}
