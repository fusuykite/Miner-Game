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
         ORE_Visit ore_visit = new ORE_Visit();
         MINER_NOT_FULL_Visit miner_not_full_visit = new MINER_NOT_FULL_Visit();
         MINER_FULL_Visit miner_full_visit = new MINER_FULL_Visit();
         ORE_BLOB_Visit ore_blob_visit = new ORE_BLOB_Visit();
         VEIN_Visit vein_visit = new VEIN_Visit();

         if (entity.accept(ore_visit)) {
            ((ORE)entity).scheduleActions(world, scheduler, imageStore);
         }

         else if (entity.accept(miner_not_full_visit)) {
            ((MINER_NOT_FULL)entity).scheduleActions(world, scheduler, imageStore);
         }

         else if (entity.accept(miner_full_visit)) {
            ((MINER_FULL)entity).scheduleActions(world, scheduler, imageStore);
         }

         else if (entity.accept(ore_blob_visit)) {
            ((ORE_BLOB)entity).scheduleActions(world, scheduler, imageStore);
         }
         else if (entity.accept(vein_visit)) {
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
