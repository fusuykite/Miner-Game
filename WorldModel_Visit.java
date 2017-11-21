public class WorldModel_Visit extends AllFalseEntityVisitor {

    public Boolean visit(VEIN vein){
        return true;
    };

    public Boolean visit(ORE ore){
        return true;
    };

    public Boolean visit(BlackSmith blackSmith){
        return true;
    };

}
