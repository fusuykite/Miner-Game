public class Activity_Visit  extends AllFalseEntityVisitor{

    public Boolean visit(ORE ore){
        return true;
    };

    public Boolean visit(Obstacle obstacle){return true;}

    public Boolean visit(VEIN vein){
        return true;
    };

    public Boolean visit(Quake quake){
        return true;
    };

    public Boolean visit(ORE_BLOB ore_blob){
        return true;
    };

    public Boolean visit(MINER_FULL miner_full){
        return true;
    };

    public Boolean visit(MINER_NOT_FULL miner_not_full){
        return true;
    };
}
