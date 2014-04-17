package hw8;

import java.util.Scanner;

import Fsm.Action;
import Fsm.Event;
import Fsm.FSM;
import Fsm.FsmException;
import Fsm.State;
import Fsm.Transition;

public class Main {
  // ===states
  State CLOSED = new CLOSED("CLOSED");
  State LISTEN = new LISTEN("LISTEN");
  State SYN_RCVD = new SYN_RCVD("SYN_RCVD");
  State SYN_SENT = new SYN_SENT("SYN_SENT");
  State ESTABLISHED = new ESTABLISHED("ESTABLISHED");
  State FIN_WAIT_1 = new FIN_WAIT_1("FIN_WAIT_1");
  State FIN_WAIT_2 = new FIN_WAIT_2("FIN_WAIT_2");
  State CLOSING = new CLOSING("CLOSING");
  State TIME_WAIT = new TIME_WAIT("TIME_WAIT");
  State EMPTY = new EMPTY("EMPTY");
  State LAST_ACK = new LAST_ACK("LAST_ACK");

  FSM fsm = new FSM("TCP", LISTEN);

  // ===events
  Event PASSIVE = new PASSIVE("PASSIVE");
  Event ACTIVE = new ACTIVE("ACTIVE");
  Event SYN = new SYN("SYN");
  Event SYNACK = new SYNACK("SYNACK");
  Event RDATA = new RDATA("RDATA");
  Event SDATA = new SDATA("SDATA");
  Event FIN = new FIN("FIN");
  Event CLOSE = new CLOSE("CLOSE");
  Event TIMEOUT = new TIMEOUT("TIMEOUT");
  Event ACK = new ACK("ACK");

  // ====actions
  Action syn_action = new syn_action();
  Action syn_ack = new syn_ack();
  Action ack = new ack_action();
  Action fin_action = new fin_action();
  Action rdatan = new rdatan();
  Action sdatan = new sdatan();
  Action noAction = new noAction();

  // =====transitions
  Transition closed_Synsent = new Transition(CLOSED, ACTIVE, SYN_SENT,
      syn_action);
  Transition listen_Closed = new Transition(LISTEN, CLOSE, CLOSED, noAction);
  Transition listen_Synrcvd = new Transition(LISTEN, SYN, SYN_RCVD, syn_ack);
  Transition closed_Listen = new Transition(CLOSED, PASSIVE, LISTEN, noAction);
  Transition synsent_Closed = new Transition(SYN_SENT, CLOSE, CLOSED, noAction);
  Transition synsent_Synrcvd = new Transition(SYN_SENT, SYN, SYN_RCVD, syn_ack);
  Transition synrcvd_Established = new Transition(SYN_RCVD, ACK, ESTABLISHED,
      noAction);
  Transition synsent_Established = new Transition(SYN_SENT, SYNACK,
      ESTABLISHED, ack);
  Transition synrcvd_Finwait1 = new Transition(SYN_RCVD, CLOSE, FIN_WAIT_1,
      fin_action);
  Transition established_Finwait1 = new Transition(ESTABLISHED, CLOSE,
      FIN_WAIT_1, fin_action);
  Transition established_empty = new Transition(ESTABLISHED, FIN, EMPTY, ack);
  Transition established_established_r = new Transition(ESTABLISHED, RDATA,
      ESTABLISHED, rdatan);
  Transition established_established_s = new Transition(ESTABLISHED, SDATA,
      ESTABLISHED, sdatan);
  Transition finwait1_finwait2 = new Transition(FIN_WAIT_1, ACK, FIN_WAIT_2,
      noAction);
  Transition finwait1_closing = new Transition(FIN_WAIT_1, FIN, CLOSING, ack);
  Transition finwait2_timewait = new Transition(FIN_WAIT_2, FIN, TIME_WAIT, ack);
  Transition closing_timewait = new Transition(CLOSING, ACK, TIME_WAIT,
      noAction);
  Transition timewait_closed = new Transition(TIME_WAIT, TIMEOUT, CLOSED,
      noAction);
  Transition empty_lastack = new Transition(EMPTY, CLOSE, LAST_ACK, fin_action);
  Transition lastack_closed = new Transition(LAST_ACK, ACK, CLOSED, noAction);

  public Main() {
    try {
      fsm.addTransition(closed_Synsent);
      fsm.addTransition(listen_Closed);
      fsm.addTransition(listen_Synrcvd);
      fsm.addTransition(closed_Listen);
      fsm.addTransition(synsent_Closed);
      fsm.addTransition(synsent_Synrcvd);
      fsm.addTransition(synrcvd_Established);
      fsm.addTransition(synsent_Established);
      fsm.addTransition(synrcvd_Finwait1);
      fsm.addTransition(established_Finwait1);
      fsm.addTransition(established_empty);
      fsm.addTransition(established_established_r);
      fsm.addTransition(established_established_s);
      fsm.addTransition(finwait1_finwait2);
      fsm.addTransition(finwait1_closing);
      fsm.addTransition(finwait2_timewait);
      fsm.addTransition(closing_timewait);
      fsm.addTransition(timewait_closed);
      fsm.addTransition(empty_lastack);
      fsm.addTransition(lastack_closed);

    } catch (FsmException e) {
      e.printStackTrace();
    }

  }

  private void execute() {
    Scanner in = new Scanner(System.in);
    System.out.println("please input Event.");
    while (in.hasNextLine()) {
      String s = in.nextLine();
      for (String event : s.split("\\s+")) {
        Event inputEvent = null;
        switch (event) {
        case "PASSIVE":
          inputEvent = new PASSIVE(event);
          break;
        case "ACTIVE":
          inputEvent = new ACTIVE(event);
          break;
        case "SYN":
          inputEvent = new SYN(event);
          break;
        case "SYNACK":
          inputEvent = new SYNACK(event);
          break;
        case "RDATA":
          inputEvent = new RDATA(event);
          break;
        case "SDATA":
          inputEvent = new SDATA(event);
          break;
        case "FIN":
          inputEvent = new FIN(event);
          break;
        case "CLOSE":
          inputEvent = new CLOSE(event);
          break;
        case "TIMEOUT":
          inputEvent = new TIMEOUT(event);
          break;
        case "ACK":
          inputEvent = new ACK(event);
          break;
        default:
          inputEvent = new BADEVENT(event);
        }// switch
        try{
        fsm.doEvent(inputEvent);
        }catch(FsmException e){
          System.err.println(e);
        }
      }// for
    }
    in.close();
  }

  public static void main(String[] args) {
    Main m = new Main();
    m.execute();
  }
}
