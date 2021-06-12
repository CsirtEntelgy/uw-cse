# Part 3 of UWCSE's Project 3
#
# based on Lab Final from UCSC's Networking Class
# which is based on of_tutorial by James McCauley

from pox.core import core
import pox.openflow.libopenflow_01 as of
from pox.lib.addresses import IPAddr, IPAddr6, EthAddr

log = core.getLogger()

#statically allocate a routing table for hosts
#MACs used in only in part 4
IPS = {
  "h10" : ("10.0.1.10", '00:00:00:00:00:01'),
  "h20" : ("10.0.2.20", '00:00:00:00:00:02'),
  "h30" : ("10.0.3.30", '00:00:00:00:00:03'),
  "serv1" : ("10.0.4.10", '00:00:00:00:00:04'),
  "hnotrust" : ("172.16.10.100", '00:00:00:00:00:05'),
}

class Part3Controller (object):
  """
  A Connection object for that switch is passed to the __init__ function.
  """
  def __init__ (self, connection):
    print (connection.dpid)
    # Keep track of the connection to the switch so that we can
    # send it messages!
    self.connection = connection

    # This binds our PacketIn event listener
    connection.addListeners(self)
    #use the dpid to figure out what switch is being created
    if (connection.dpid == 1):
      self.s1_setup()
    elif (connection.dpid == 2):
      self.s2_setup()
    elif (connection.dpid == 3):
      self.s3_setup()
    elif (connection.dpid == 21):
      self.cores21_setup()
    elif (connection.dpid == 31):
      self.dcs31_setup()
    else:
      print ("UNKNOWN SWITCH")
      exit(1)

  def s1_setup(self):
    self.flood_helper(2, True)
    self.flood_helper(1, False)

  def s2_setup(self):
    self.flood_helper(2, True)
    self.flood_helper(1, False)

  def s3_setup(self):
    self.flood_helper(2, True)
    self.flood_helper(1, False)

  def cores21_setup(self):
    self.block_helper_ICMP(IPS['hnotrust'][0])  #block ICMP
    self.block_helper_IP(IPS['hnotrust'][0])    #block IP

    self.entry_helper(1, IPS['h10'][0])         #host 10
    self.entry_helper(2, IPS['h20'][0])         #host 20
    self.entry_helper(3, IPS['h30'][0])         #host 30
    self.entry_helper(4, IPS['serv1'][0])       #serv1
    self.entry_helper(5, IPS['hnotrust'][0])    #hnotrust

    self.flood_helper(2, True)
    self.flood_helper(1, False)

  def dcs31_setup(self):
    self.flood_helper(2, True)
    self.flood_helper(1, False)

  def flood_helper(self, prio, flood):
    msg = of.ofp_flow_mod()
    msg.priority = prio
    if flood: # flood or hang
      msg.actions.append(of.ofp_action_output(port=of.OFPP_FLOOD))
    self.connection.send(msg)

  def block_helper_ICMP(self, block):
    msg = of.ofp_flow_mod()
    msg.priority = 5
    msg.match.dl_type = 0x0800 #IPv4
    msg.match.nw_proto = 1 #ICMP
    msg.match.nw_src = block
    self.connection.send(msg)

  def block_helper_IP(self, block):
    msg = of.ofp_flow_mod()
    msg.priority = 4
    msg.match.dl_type = 0x0800 #IPv4
    msg.match.nw_src = block
    msg.match.nw_dst = IPS['serv1'][0]
    self.connection.send(msg)

  def entry_helper(self, p, dst):
    msg = of.ofp_flow_mod()
    msg.priority = 3
    msg.match.dl_type = 0x0800
    msg.match.nw_dst = dst
    msg.actions.append(of.ofp_action_output(port=p))
    self.connection.send(msg)

  #used in part 4 to handle individual ARP packets
  #not needed for part 3 (USE RULES!)
  #causes the switch to output packet_in on out_port
  def resend_packet(self, packet_in, out_port):
    msg = of.ofp_packet_out()
    msg.data = packet_in
    action = of.ofp_action_output(port = out_port)
    msg.actions.append(action)
    self.connection.send(msg)

  def _handle_PacketIn (self, event):
    """
    Packets not handled by the router rules will be
    forwarded to this method to be handled by the controller
    """

    packet = event.parsed # This is the parsed packet data.
    if not packet.parsed:
      log.warning("Ignoring incomplete packet")
      return

    packet_in = event.ofp # The actual ofp_packet_in message.
    print ("Unhandled packet from " + str(self.connection.dpid) + ":" + packet.dump())

def launch ():
  """
  Starts the component
  """
  def start_switch (event):
    log.debug("Controlling %s" % (event.connection,))
    Part3Controller(event.connection)
  core.openflow.addListenerByName("ConnectionUp", start_switch)