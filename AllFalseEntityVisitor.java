import com.sun.org.apache.xpath.internal.operations.Bool;

public class AllFalseEntityVisitor implements EntityVisitor<Boolean>{

    public Boolean visit(ORE ore){
        return false;
    };

    public Boolean visit(BlackSmith blackSmith){
        return false;
    };

    public Boolean visit(Obstacle obstacle){
        return false;
    };

    public Boolean visit(Santa santa){return false;}

    public Boolean visit(VEIN vein){
        return false;
    };

    public Boolean visit(Quake quake){
        return false;
    };

    public Boolean visit(ORE_BLOB ore_blob){
        return false;
    };

    public Boolean visit(MINER_FULL miner_full){
        return false;
    };

    public Boolean visit(MINER_NOT_FULL miner_not_full){
        return false;
    };

    public Boolean visit(Ornaments ornaments){return false;};

}
