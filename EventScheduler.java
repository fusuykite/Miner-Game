import java.util.*;

final class EventScheduler
{
   public static final Random rand = new Random();

   protected PriorityQueue<Event> eventQueue;
   protected Map<Entity, List<Event>> pendingEvents;
   protected double timeScale;






   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void scheduleEvent(Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * timeScale);
      Event event = new Event(action, time, entity);

      eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      pendingEvents.put(entity, pending);
   }



   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            eventQueue.remove(event);
         }
      }
   }

   public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.entities)
      {
         if (entity instanceof ORE) {
            ((ORE)entity).scheduleActions(world, scheduler, imageStore);
         }
         else if (entity instanceof MINER_NOT_FULL) {
            ((MINER_NOT_FULL)entity).scheduleActions(world, scheduler, imageStore);
         }
         else if (entity instanceof MINER_FULL) {
            ((MINER_FULL)entity).scheduleActions(world, scheduler, imageStore);
         }
         else if (entity instanceof ORE_BLOB) {
            ((ORE_BLOB)entity).scheduleActions(world, scheduler, imageStore);
         }
         else if (entity instanceof VEIN) {
            ((VEIN)entity).scheduleActions(world, scheduler, imageStore);
         }
      }
   }

   public void removePendingEvent(Event event)
   {
      List<Event> pending = pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public static void updateOnTime(EventScheduler scheduler, long time)
   {
      while (!scheduler.eventQueue.isEmpty() &&
              scheduler.eventQueue.peek().time < time)
      {
         Event next = scheduler.eventQueue.poll();

         scheduler.removePendingEvent(next);

         next.action.executeAction(scheduler);
      }
   }






}
