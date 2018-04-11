package model.existdb;
public class Item
{
    private DateTime date;

    private String collection;

    private String title;

    private String objid;

    private String display-date;

    private Hits hits;

    private Summaries summaries;

    public void setDate(DateTime date){
        this.date = date;
    }
    public DateTime getDate(){
        return this.date;
    }
    public void setCollection(String collection){
        this.collection = collection;
    }
    public String getCollection(){
        return this.collection;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setObjid(String objid){
        this.objid = objid;
    }
    public String getObjid(){
        return this.objid;
    }
    public void setDisplay-date(String display-date){
    this.display-date = display-date;
}
    public String getDisplay-date(){
    return this.display-date;
}
    public void setHits(Hits hits){
        this.hits = hits;
    }
    public Hits getHits(){
        return this.hits;
    }
    public void setSummaries(Summaries summaries){
        this.summaries = summaries;
    }
    public Summaries getSummaries(){
        return this.summaries;
    }
}
