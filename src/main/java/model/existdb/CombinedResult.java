package model.existdb;
import java.util.ArrayList;
import java.util.List;
public class CombinedResult
{
    private String collectionsSearched;

    private List<CollectionResults> collectionResults;

    public void setCollectionsSearched(String collectionsSearched){
        this.collectionsSearched = collectionsSearched;
    }
    public String getCollectionsSearched(){
        return this.collectionsSearched;
    }
    public void setCollectionResults(List<CollectionResults> collectionResults){
        this.collectionResults = collectionResults;
    }
    public List<CollectionResults> getCollectionResults(){
        return this.collectionResults;
    }
}