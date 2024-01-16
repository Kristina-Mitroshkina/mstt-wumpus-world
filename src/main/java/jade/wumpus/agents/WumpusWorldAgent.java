package jade.wumpus.agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wumpus.actions.Action;
import jade.wumpus.actions.ActionResult;
import jade.wumpus.actions.ActionType;
import jade.wumpus.actions.Climb;
import jade.wumpus.actions.GrabItem;
import jade.wumpus.actions.MoveForward;
import jade.wumpus.actions.Shoot;
import jade.wumpus.actions.TurnLeft;
import jade.wumpus.actions.TurnRight;
import jade.wumpus.behaviours.world.WorldToSpeleologistCommunication;
import jade.wumpus.behaviours.world.WumpusGameInformationBehaviour;
import jade.wumpus.behaviours.world.WumpusWorldCoreGameLoop;
import jade.wumpus.configs.WorldConfiguration;
import jade.wumpus.world.AgentPosition;
import jade.wumpus.world.Percept;
import jade.wumpus.world.Position;
import jade.wumpus.world.Room;
import jade.wumpus.world.RoomContent;
import jade.wumpus.world.WumpusCave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WumpusWorldAgent extends Agent {
    private final WumpusCave cave;
    private final Map<ActionType, Action> actions = new HashMap<>();

    public WumpusWorldAgent() {

        this.cave = new WumpusCave();

        this.actions.put(ActionType.TURN_LEFT, new TurnLeft(this.cave));
        this.actions.put(ActionType.TURN_RIGHT, new TurnRight(this.cave));
        this.actions.put(ActionType.MOVE_FORWARD, new MoveForward(this.cave));
        this.actions.put(ActionType.SHOOT, new Shoot(this.cave));
        this.actions.put(ActionType.GRAB, new GrabItem(this.cave));
        this.actions.put(ActionType.CLIMB, new Climb(this.cave));
        // add climb
    }

    @Override
    protected void setup() {
        System.out.println("[" + getAID().getName() + "] agent is ready.");

        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(WorldConfiguration.WUMPUS_WORLD_TYPE);
        serviceDescription.setName(WorldConfiguration.WUMPUS_WORLD_AGENT_SERVICE_DESCRIPTION);
        agentDescription.addServices(serviceDescription);

        try {
            DFService.register(this, agentDescription);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new WumpusWorldCoreGameLoop(this));
        addBehaviour(new WumpusGameInformationBehaviour(this));
        addBehaviour(new WorldToSpeleologistCommunication(this));
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Agent " + getAID().getName() + " shutting down.");
    }

    public ActionResult doAction(ActionType actionKey) {
        final Action action = this.actions.get(actionKey);
        if (action == null) {
            throw new RuntimeException("No such action " + actionKey.toString());
        }

        ActionResult result = action.act();
        return ActionResult.OK;
    }

    public boolean isAgentAlive() {
        return this.cave.getAgent().isAlive();

    }

    public Percept percept() {
        Percept result = new Percept();
        AgentPosition agentPosition = this.cave.getAgent().getPosition();
        Position pos = agentPosition.getPosition();

        final Room toTheLeft = this.cave.getRoomAt(new Position(pos.getX() - 1, pos.getY()));
        final Room atTheTop = this.cave.getRoomAt(new Position(pos.getX(), pos.getY() + 1));
        final Room toTheRight = this.cave.getRoomAt(new Position(pos.getX() + 1, pos.getY()));
        final Room atTheBottom = this.cave.getRoomAt(new Position(pos.getX(), pos.getY() - 1));

        List<Room> roomsToCheck = new ArrayList<>();

        if (toTheLeft != null)
            roomsToCheck.add(toTheLeft);
        if (atTheTop != null)
            roomsToCheck.add(atTheTop);
        if (toTheRight != null)
            roomsToCheck.add(toTheRight);
        if (atTheBottom != null)
            roomsToCheck.add(atTheBottom);

        for (Room room : roomsToCheck) {
            if (room.getContent().equals(RoomContent.WUMPUS)) {
                result.wumpusIsClose();
            }

            if (this.cave.isFacingWumpus(agentPosition)) {
                result.heardScream();
            }

            if (room.getContent().equals(RoomContent.PIT)) {
                result.pitIsClose();
            }

            if (room.getContent().equals(RoomContent.BREEZE)) {
                result.breezeIsClose();
            }

            if (room.getContent().equals(RoomContent.STENCH)) {
                result.stenchIsClose();
            }

            if (room.getContent().equals(RoomContent.GOLD)) {
                result.goldIsClose();
            }
        }

        return result;
    }
}
