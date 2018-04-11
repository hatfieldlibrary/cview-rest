package model.existdb;
public class CollectionResults
{
    private String collectionName;

    private Result result;

    public void setCollectionName(String collectionName){
        this.collectionName = collectionName;
    }
    public String getCollectionName(){
        return this.collectionName;
    }
    public void setResult(Result result){
        this.result = result;
    }
    public Result getResult(){
        return this.result;
    }
}