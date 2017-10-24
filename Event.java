final class Event
{
   public ActionInterface action;
   public long time;
   public EntityInterface entity;

   public Event(ActionInterface action, long time, EntityInterface entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }
}
