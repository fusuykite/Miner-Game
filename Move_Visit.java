public class Move_Visit extends AllFalseEntityVisitor {

    public Boolean visit(MINER_FULL miner_full){
        return true;
    }

    public Boolean visit(MINER_NOT_FULL miner_not_full){
        return true;
    }
}