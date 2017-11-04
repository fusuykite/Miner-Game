public abstract class ActionAb implements Action {

    protected Entity entity;

    public abstract void executeAction(EventScheduler scheduler);


    public Entity getEntity(){
        return entity;
    }


}
