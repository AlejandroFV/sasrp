
import java.awt.Color;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro
 */
public class SeatSelection extends javax.swing.JFrame {
    private static String userFirstName;
    private static String userLastName;
    private static LinkedList reservedSeats = new LinkedList();
    
    /**
     * Creates new form SeatSelection
     */
    public SeatSelection(String userFirstName, String userLastName) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initComponents();
        setLocationRelativeTo(null);
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        paintAllSeats();
    }
    
    private static void paintAllSeats() {
        javax.swing.JToggleButton[] seats =
        {a1Button, a2Button, a3Button, a4Button, a5Button, a6Button, a7Button, a8Button, a9Button, a10Button, 
        b1Button, b2Button, b3Button, b4Button, b5Button, b6Button, b7Button, b8Button, b9Button, b10Button, 
        c1Button, c2Button, c3Button, c4Button, c5Button, c6Button, c7Button, c8Button, c9Button, c10Button, 
        d1Button, d2Button, d3Button, d4Button, d5Button, d6Button, d7Button, d8Button, d9Button, d10Button, 
        e1Button, e2Button, e3Button, e4Button, e5Button, e6Button, e7Button, e8Button, e9Button, e10Button, 
        f1Button, f2Button, f3Button, f4Button, f5Button, f6Button, f7Button, f8Button, f9Button, f10Button, 
        g1Button, g2Button, g3Button, g4Button, g5Button, g6Button, g7Button, g8Button, g9Button, g10Button, 
        h1Button, h2Button, h3Button, h4Button, h5Button, h6Button, h7Button, h8Button, h9Button, h10Button, 
        i1Button, i2Button, i3Button, i4Button, i5Button, i6Button, i7Button, i8Button, i9Button, i10Button, 
        j1Button, j2Button, j3Button, j4Button, j5Button, j6Button, j7Button, j8Button, j9Button, j10Button};
        
        for (int i = 0; i < 100; i++) {
            paintSeat(seats[i]);
        }
    }
    
    private static void paintSeat(javax.swing.JToggleButton button) {
        try {            
            Registry registry = LocateRegistry.getRegistry("127.0.0.1");
            IRemoteSeat rs = (IRemoteSeat) registry.lookup("Seat");
            ArrayList <Seat> arrSeat = rs.findSeatByName(button.getText());
            System.out.print(button.getText());
            int id = 0;
            String status = null;
            for (Seat s : arrSeat) {
                id = s.getId();
                status = s.getStatus();
                System.out.println(" - " + status);
            }
            IRemoteReservation rr = (IRemoteReservation) registry.lookup("Reservation");
            IRemoteUser ru = (IRemoteUser) registry.lookup("User");
            ArrayList <Reservation> arrRes = rr.findReservationBySeatID(id);
            int userID = 0;
            for (Reservation r : arrRes) {
                userID = r.getUserID();
            }
            ArrayList <User> arrUser = ru.findUserByID(userID);
            String firstName = null;
            String lastName = null;
            for (User u : arrUser) {
                firstName = u.getFirstName();
                lastName = u.getLastName();
            }
            switch (status) {
                case "available":
                    button.setBackground(Color.GREEN);
                    break;
                case "reserved":
                    if (firstName.equals(userFirstName) && lastName.equals(userLastName)) {
                        button.setBackground(Color.BLACK);
                    } else {
                        button.setBackground(Color.RED);
                    }
                    break;
                case "in-process":
                    if (firstName.equals(userFirstName) && lastName.equals(userLastName)) {
                        button.setBackground(Color.BLACK);
                    } else {
                        button.setBackground(Color.BLUE);
                    }
                    break;
            }
            setSeatAvailability(button);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void setSeatAvailability(javax.swing.JToggleButton button) {
        if (button.getBackground() == Color.RED || button.getBackground() == Color.BLUE) {
            button.setSelected(true);
        }
        if (button.getBackground() == Color.BLACK) {
            button.setEnabled(true);
            button.setSelected(true);
        }
        if (button.getBackground() == Color.GREEN) {
            button.setEnabled(true);
            button.setSelected(false);
        }
    }
    
    private static void reserveSeat(javax.swing.JToggleButton button) {
        if (button.getBackground() == Color.RED ||
                button.getBackground() == Color.BLUE) {
            JOptionPane.showMessageDialog(button,
                    "Este asiento no se encuentra disponible para reservación",
                    "Operación inválida", JOptionPane.WARNING_MESSAGE);
            button.setSelected(true);
        }
        if (button.getBackground() == Color.GREEN) {
            try {
                Registry registry = LocateRegistry.getRegistry("127.0.0.1");
                IRemoteSeat rs = (IRemoteSeat) registry.lookup("Seat");
                IRemoteReservation rr = (IRemoteReservation) registry.lookup("Reservation");
                IRemoteUser ru = (IRemoteUser) registry.lookup("User");
                ArrayList <User> arrUser = ru.findUserByName(userFirstName,
                        userLastName);
                int userID = 0;
                for (User u : arrUser) {
                    userID = u.getId();
                }
                ArrayList <Seat> arrSeat = rs.findSeatByName(button.getText());
                int seatID = 0;
                for (Seat s : arrSeat) {
                    seatID = s.getId();
                }
                Seat reservedSeat = new Seat(button.getText(), "in-process");
                rs.updateSeat(reservedSeat);
                Reservation reservation = new Reservation(userID, seatID);
                rr.saveReservation(reservation);
                button.setBackground(Color.BLACK);
                button.setSelected(true);
                manageReservedSeatsLimit(button);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (button.getBackground() == Color.BLACK) {
            try {
                Registry registry = LocateRegistry.getRegistry("127.0.0.1");
                IRemoteSeat rs = (IRemoteSeat) registry.lookup("Seat");
                IRemoteReservation rr = (IRemoteReservation) registry.lookup("Reservation");
                IRemoteUser ru = (IRemoteUser) registry.lookup("User");
                ArrayList <User> arrUser = ru.findUserByName(userFirstName,
                        userLastName);
                int userID = 0;
                for (User u : arrUser) {
                    userID = u.getId();
                }
                ArrayList <Seat> arrSeat = rs.findSeatByName(button.getText());
                int seatID = 0;
                for (Seat s : arrSeat) {
                    seatID = s.getId();
                }
                Seat reservedSeat = new Seat(button.getText(), "available");
                rs.updateSeat(reservedSeat);
                Reservation reservation = new Reservation(userID, seatID);
                rr.deleteReservation(reservation);
                button.setBackground(Color.GREEN);
                button.setSelected(false);
                manageReservedSeatsLimit(button);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }
    
    private static void manageReservedSeatsLimit(javax.swing.JToggleButton button) {
        if (button.getBackground() == Color.GREEN) {
            reservedSeats.remove(button);
        } else if (button.getBackground() == Color.BLACK) {
            if (reservedSeats.size() < 5) {
                reservedSeats.add(button);
            } else {
                try {
                    javax.swing.JToggleButton topButton =
                            (javax.swing.JToggleButton) reservedSeats.getFirst();
                    Registry registry = LocateRegistry.getRegistry("127.0.0.1");
                    IRemoteSeat rs = (IRemoteSeat) registry.lookup("Seat");
                    IRemoteReservation rr = (IRemoteReservation) registry.lookup("Reservation");
                    IRemoteUser ru = (IRemoteUser) registry.lookup("User");
                    ArrayList <User> arrUser = ru.findUserByName(userFirstName,
                            userLastName);
                    int userID = 0;
                    for (User u : arrUser) {
                        userID = u.getId();
                    }
                    ArrayList <Seat> arrSeat = rs.findSeatByName(topButton.getText());
                    int seatID = 0;
                    for (Seat s : arrSeat) {
                        seatID = s.getId();
                    }
                    Seat reservedSeat = new Seat(topButton.getText(), "available");
                    rs.updateSeat(reservedSeat);
                    Reservation reservation = new Reservation(userID, seatID);
                    rr.deleteReservation(reservation);
                    topButton.setBackground(Color.GREEN);
                    topButton.setSelected(false);
                    reservedSeats.removeFirst();
                    reservedSeats.add(button);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private SeatSelection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        a1Button = new javax.swing.JToggleButton();
        a2Button = new javax.swing.JToggleButton();
        a3Button = new javax.swing.JToggleButton();
        a4Button = new javax.swing.JToggleButton();
        a5Button = new javax.swing.JToggleButton();
        a6Button = new javax.swing.JToggleButton();
        a7Button = new javax.swing.JToggleButton();
        a8Button = new javax.swing.JToggleButton();
        a9Button = new javax.swing.JToggleButton();
        a10Button = new javax.swing.JToggleButton();
        b3Button = new javax.swing.JToggleButton();
        b4Button = new javax.swing.JToggleButton();
        b1Button = new javax.swing.JToggleButton();
        b5Button = new javax.swing.JToggleButton();
        b6Button = new javax.swing.JToggleButton();
        b7Button = new javax.swing.JToggleButton();
        b8Button = new javax.swing.JToggleButton();
        b9Button = new javax.swing.JToggleButton();
        b10Button = new javax.swing.JToggleButton();
        b2Button = new javax.swing.JToggleButton();
        c3Button = new javax.swing.JToggleButton();
        d5Button = new javax.swing.JToggleButton();
        c4Button = new javax.swing.JToggleButton();
        d6Button = new javax.swing.JToggleButton();
        c5Button = new javax.swing.JToggleButton();
        d7Button = new javax.swing.JToggleButton();
        c6Button = new javax.swing.JToggleButton();
        d8Button = new javax.swing.JToggleButton();
        c7Button = new javax.swing.JToggleButton();
        d9Button = new javax.swing.JToggleButton();
        c8Button = new javax.swing.JToggleButton();
        d10Button = new javax.swing.JToggleButton();
        c9Button = new javax.swing.JToggleButton();
        d2Button = new javax.swing.JToggleButton();
        c10Button = new javax.swing.JToggleButton();
        c1Button = new javax.swing.JToggleButton();
        d3Button = new javax.swing.JToggleButton();
        d4Button = new javax.swing.JToggleButton();
        c2Button = new javax.swing.JToggleButton();
        d1Button = new javax.swing.JToggleButton();
        e7Button = new javax.swing.JToggleButton();
        e8Button = new javax.swing.JToggleButton();
        e9Button = new javax.swing.JToggleButton();
        e10Button = new javax.swing.JToggleButton();
        e2Button = new javax.swing.JToggleButton();
        e3Button = new javax.swing.JToggleButton();
        e4Button = new javax.swing.JToggleButton();
        e1Button = new javax.swing.JToggleButton();
        e5Button = new javax.swing.JToggleButton();
        e6Button = new javax.swing.JToggleButton();
        h5Button = new javax.swing.JToggleButton();
        i7Button = new javax.swing.JToggleButton();
        h6Button = new javax.swing.JToggleButton();
        i8Button = new javax.swing.JToggleButton();
        h7Button = new javax.swing.JToggleButton();
        i9Button = new javax.swing.JToggleButton();
        h8Button = new javax.swing.JToggleButton();
        i10Button = new javax.swing.JToggleButton();
        h9Button = new javax.swing.JToggleButton();
        i2Button = new javax.swing.JToggleButton();
        h10Button = new javax.swing.JToggleButton();
        h1Button = new javax.swing.JToggleButton();
        i3Button = new javax.swing.JToggleButton();
        i4Button = new javax.swing.JToggleButton();
        h2Button = new javax.swing.JToggleButton();
        i1Button = new javax.swing.JToggleButton();
        f1Button = new javax.swing.JToggleButton();
        j7Button = new javax.swing.JToggleButton();
        j8Button = new javax.swing.JToggleButton();
        j9Button = new javax.swing.JToggleButton();
        j10Button = new javax.swing.JToggleButton();
        j2Button = new javax.swing.JToggleButton();
        f2Button = new javax.swing.JToggleButton();
        j3Button = new javax.swing.JToggleButton();
        f3Button = new javax.swing.JToggleButton();
        j4Button = new javax.swing.JToggleButton();
        f4Button = new javax.swing.JToggleButton();
        j1Button = new javax.swing.JToggleButton();
        f5Button = new javax.swing.JToggleButton();
        j5Button = new javax.swing.JToggleButton();
        f6Button = new javax.swing.JToggleButton();
        j6Button = new javax.swing.JToggleButton();
        f7Button = new javax.swing.JToggleButton();
        f8Button = new javax.swing.JToggleButton();
        f9Button = new javax.swing.JToggleButton();
        f10Button = new javax.swing.JToggleButton();
        g3Button = new javax.swing.JToggleButton();
        g4Button = new javax.swing.JToggleButton();
        g1Button = new javax.swing.JToggleButton();
        g5Button = new javax.swing.JToggleButton();
        g6Button = new javax.swing.JToggleButton();
        g7Button = new javax.swing.JToggleButton();
        g8Button = new javax.swing.JToggleButton();
        g9Button = new javax.swing.JToggleButton();
        g10Button = new javax.swing.JToggleButton();
        g2Button = new javax.swing.JToggleButton();
        h3Button = new javax.swing.JToggleButton();
        i5Button = new javax.swing.JToggleButton();
        h4Button = new javax.swing.JToggleButton();
        i6Button = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        finishTransactionButton = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Seleccionar asientos");
        setForeground(java.awt.SystemColor.activeCaption);
        setResizable(false);

        a1Button.setText("A1");
        a1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a1ButtonActionPerformed(evt);
            }
        });

        a2Button.setText("A2");
        a2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a2ButtonActionPerformed(evt);
            }
        });

        a3Button.setText("A3");
        a3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a3ButtonActionPerformed(evt);
            }
        });

        a4Button.setText("A4");
        a4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a4ButtonActionPerformed(evt);
            }
        });

        a5Button.setText("A5");
        a5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a5ButtonActionPerformed(evt);
            }
        });

        a6Button.setText("A6");
        a6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a6ButtonActionPerformed(evt);
            }
        });

        a7Button.setText("A7");
        a7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a7ButtonActionPerformed(evt);
            }
        });

        a8Button.setText("A8");
        a8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a8ButtonActionPerformed(evt);
            }
        });

        a9Button.setText("A9");
        a9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a9ButtonActionPerformed(evt);
            }
        });

        a10Button.setText("A10");
        a10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a10ButtonActionPerformed(evt);
            }
        });

        b3Button.setText("B3");
        b3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b3ButtonActionPerformed(evt);
            }
        });

        b4Button.setText("B4");
        b4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b4ButtonActionPerformed(evt);
            }
        });

        b1Button.setText("B1");
        b1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1ButtonActionPerformed(evt);
            }
        });

        b5Button.setText("B5");
        b5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b5ButtonActionPerformed(evt);
            }
        });

        b6Button.setText("B6");
        b6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b6ButtonActionPerformed(evt);
            }
        });

        b7Button.setText("B7");
        b7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b7ButtonActionPerformed(evt);
            }
        });

        b8Button.setText("B8");
        b8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b8ButtonActionPerformed(evt);
            }
        });

        b9Button.setText("B9");
        b9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b9ButtonActionPerformed(evt);
            }
        });

        b10Button.setText("B10");
        b10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b10ButtonActionPerformed(evt);
            }
        });

        b2Button.setText("B2");
        b2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b2ButtonActionPerformed(evt);
            }
        });

        c3Button.setText("C3");
        c3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c3ButtonActionPerformed(evt);
            }
        });

        d5Button.setText("D5");
        d5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d5ButtonActionPerformed(evt);
            }
        });

        c4Button.setText("C4");
        c4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c4ButtonActionPerformed(evt);
            }
        });

        d6Button.setText("D6");
        d6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d6ButtonActionPerformed(evt);
            }
        });

        c5Button.setText("C5");
        c5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c5ButtonActionPerformed(evt);
            }
        });

        d7Button.setText("D7");
        d7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d7ButtonActionPerformed(evt);
            }
        });

        c6Button.setText("C6");
        c6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c6ButtonActionPerformed(evt);
            }
        });

        d8Button.setText("D8");
        d8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d8ButtonActionPerformed(evt);
            }
        });

        c7Button.setText("C7");
        c7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c7ButtonActionPerformed(evt);
            }
        });

        d9Button.setText("D9");
        d9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d9ButtonActionPerformed(evt);
            }
        });

        c8Button.setText("C8");
        c8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c8ButtonActionPerformed(evt);
            }
        });

        d10Button.setText("D10");
        d10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d10ButtonActionPerformed(evt);
            }
        });

        c9Button.setText("C9");
        c9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c9ButtonActionPerformed(evt);
            }
        });

        d2Button.setText("D2");
        d2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d2ButtonActionPerformed(evt);
            }
        });

        c10Button.setText("C10");
        c10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c10ButtonActionPerformed(evt);
            }
        });

        c1Button.setText("C1");
        c1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c1ButtonActionPerformed(evt);
            }
        });

        d3Button.setText("D3");
        d3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d3ButtonActionPerformed(evt);
            }
        });

        d4Button.setText("D4");
        d4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d4ButtonActionPerformed(evt);
            }
        });

        c2Button.setText("C2");
        c2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c2ButtonActionPerformed(evt);
            }
        });

        d1Button.setText("D1");
        d1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d1ButtonActionPerformed(evt);
            }
        });

        e7Button.setText("E7");
        e7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e7ButtonActionPerformed(evt);
            }
        });

        e8Button.setText("E8");
        e8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e8ButtonActionPerformed(evt);
            }
        });

        e9Button.setText("E9");
        e9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e9ButtonActionPerformed(evt);
            }
        });

        e10Button.setText("E10");
        e10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e10ButtonActionPerformed(evt);
            }
        });

        e2Button.setText("E2");
        e2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e2ButtonActionPerformed(evt);
            }
        });

        e3Button.setText("E3");
        e3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e3ButtonActionPerformed(evt);
            }
        });

        e4Button.setText("E4");
        e4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e4ButtonActionPerformed(evt);
            }
        });

        e1Button.setText("E1");
        e1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e1ButtonActionPerformed(evt);
            }
        });

        e5Button.setText("E5");
        e5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e5ButtonActionPerformed(evt);
            }
        });

        e6Button.setText("E6");
        e6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e6ButtonActionPerformed(evt);
            }
        });

        h5Button.setText("H5");
        h5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h5ButtonActionPerformed(evt);
            }
        });

        i7Button.setText("I7");
        i7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i7ButtonActionPerformed(evt);
            }
        });

        h6Button.setText("H6");
        h6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h6ButtonActionPerformed(evt);
            }
        });

        i8Button.setText("I8");
        i8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i8ButtonActionPerformed(evt);
            }
        });

        h7Button.setText("H7");
        h7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h7ButtonActionPerformed(evt);
            }
        });

        i9Button.setText("I9");
        i9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i9ButtonActionPerformed(evt);
            }
        });

        h8Button.setText("H8");
        h8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h8ButtonActionPerformed(evt);
            }
        });

        i10Button.setText("I10");
        i10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i10ButtonActionPerformed(evt);
            }
        });

        h9Button.setText("H9");
        h9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h9ButtonActionPerformed(evt);
            }
        });

        i2Button.setText("I2");
        i2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i2ButtonActionPerformed(evt);
            }
        });

        h10Button.setText("H10");
        h10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h10ButtonActionPerformed(evt);
            }
        });

        h1Button.setText("H1");
        h1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h1ButtonActionPerformed(evt);
            }
        });

        i3Button.setText("I3");
        i3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i3ButtonActionPerformed(evt);
            }
        });

        i4Button.setText("I4");
        i4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i4ButtonActionPerformed(evt);
            }
        });

        h2Button.setText("H2");
        h2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h2ButtonActionPerformed(evt);
            }
        });

        i1Button.setText("I1");
        i1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i1ButtonActionPerformed(evt);
            }
        });

        f1Button.setText("F1");
        f1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f1ButtonActionPerformed(evt);
            }
        });

        j7Button.setText("J7");
        j7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j7ButtonActionPerformed(evt);
            }
        });

        j8Button.setText("J8");
        j8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j8ButtonActionPerformed(evt);
            }
        });

        j9Button.setText("J9");
        j9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j9ButtonActionPerformed(evt);
            }
        });

        j10Button.setText("J10");
        j10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j10ButtonActionPerformed(evt);
            }
        });

        j2Button.setText("J2");
        j2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j2ButtonActionPerformed(evt);
            }
        });

        f2Button.setText("F2");
        f2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f2ButtonActionPerformed(evt);
            }
        });

        j3Button.setText("J3");
        j3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j3ButtonActionPerformed(evt);
            }
        });

        f3Button.setText("F3");
        f3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f3ButtonActionPerformed(evt);
            }
        });

        j4Button.setText("J4");
        j4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j4ButtonActionPerformed(evt);
            }
        });

        f4Button.setText("F4");
        f4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f4ButtonActionPerformed(evt);
            }
        });

        j1Button.setText("J1");
        j1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j1ButtonActionPerformed(evt);
            }
        });

        f5Button.setText("F5");
        f5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f5ButtonActionPerformed(evt);
            }
        });

        j5Button.setText("J5");
        j5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j5ButtonActionPerformed(evt);
            }
        });

        f6Button.setText("F6");
        f6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f6ButtonActionPerformed(evt);
            }
        });

        j6Button.setText("J6");
        j6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j6ButtonActionPerformed(evt);
            }
        });

        f7Button.setText("F7");
        f7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f7ButtonActionPerformed(evt);
            }
        });

        f8Button.setText("F8");
        f8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f8ButtonActionPerformed(evt);
            }
        });

        f9Button.setText("F9");
        f9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f9ButtonActionPerformed(evt);
            }
        });

        f10Button.setText("F10");
        f10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f10ButtonActionPerformed(evt);
            }
        });

        g3Button.setText("G3");
        g3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g3ButtonActionPerformed(evt);
            }
        });

        g4Button.setText("G4");
        g4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g4ButtonActionPerformed(evt);
            }
        });

        g1Button.setText("G1");
        g1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g1ButtonActionPerformed(evt);
            }
        });

        g5Button.setText("G5");
        g5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g5ButtonActionPerformed(evt);
            }
        });

        g6Button.setText("G6");
        g6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g6ButtonActionPerformed(evt);
            }
        });

        g7Button.setText("G7");
        g7Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g7ButtonActionPerformed(evt);
            }
        });

        g8Button.setText("G8");
        g8Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g8ButtonActionPerformed(evt);
            }
        });

        g9Button.setText("G9");
        g9Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g9ButtonActionPerformed(evt);
            }
        });

        g10Button.setText("G10");
        g10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g10ButtonActionPerformed(evt);
            }
        });

        g2Button.setText("G2");
        g2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                g2ButtonActionPerformed(evt);
            }
        });

        h3Button.setText("H3");
        h3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h3ButtonActionPerformed(evt);
            }
        });

        i5Button.setText("I5");
        i5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i5ButtonActionPerformed(evt);
            }
        });

        h4Button.setText("H4");
        h4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                h4ButtonActionPerformed(evt);
            }
        });

        i6Button.setText("I6");
        i6Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                i6ButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("A");

        jLabel2.setText("B");

        jLabel3.setText("C");

        jLabel4.setText("D");

        jLabel5.setText("E");

        jLabel6.setText("F");

        jLabel7.setText("G");

        jLabel8.setText("H");

        jLabel9.setText("I");

        jLabel10.setText("J");

        jLabel11.setText("1");

        jLabel12.setText("2");

        jLabel13.setText("3");

        jLabel14.setText("4");

        jLabel15.setText("5");

        jLabel16.setText("6");

        jLabel17.setText("7");

        jLabel18.setText("8");

        jLabel19.setText("9");

        jLabel20.setText("10");

        refreshButton.setText("Actualizar");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        finishTransactionButton.setText("Reservar");
        finishTransactionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishTransactionButtonActionPerformed(evt);
            }
        });

        jLabel21.setForeground(Color.GREEN);
        jLabel21.setText("Disponible");

        jLabel22.setText("En proceso de reserva (usuario actual)");

        jLabel23.setForeground(Color.BLUE);
        jLabel23.setText("En proceso de reserva (otro usuario)");

        jLabel24.setForeground(Color.RED);
        jLabel24.setText("Reservado");

        cancelButton.setText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(i1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(i10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(h1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(h10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(g1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(g10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(f1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(j1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(j2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(j10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(60, 60, 60)
                                        .addComponent(refreshButton))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel22))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(finishTransactionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(e1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(e10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(d1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(d10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(c1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11)
                                    .addComponent(a1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(a2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(a3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(a4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(a5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(a6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel18)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(a9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(a10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20))))))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(a10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(c10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(c1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(d10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(d1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(e10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(e1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(f10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(f1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(g10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(g9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(g8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(g7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(g6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(g5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(g4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(g3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(g2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(g1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(h10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(h9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(h8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(h7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(h6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(h5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(h4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(h3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(h2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(h1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(i10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(i9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(i8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(i7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(i6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(i5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(i4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(i3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(i2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(i1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(j10Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j9Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j8Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j7Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j6Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j5Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(j1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshButton)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(finishTransactionButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel24)))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void a1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a1Button);
    }//GEN-LAST:event_a1ButtonActionPerformed

    private void a2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a2Button);
    }//GEN-LAST:event_a2ButtonActionPerformed

    private void a3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a3Button);
    }//GEN-LAST:event_a3ButtonActionPerformed

    private void a4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a4Button);
    }//GEN-LAST:event_a4ButtonActionPerformed

    private void a5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a5Button);
    }//GEN-LAST:event_a5ButtonActionPerformed

    private void a6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a6Button);
    }//GEN-LAST:event_a6ButtonActionPerformed

    private void a7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a7Button);
    }//GEN-LAST:event_a7ButtonActionPerformed

    private void a8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a8Button);
    }//GEN-LAST:event_a8ButtonActionPerformed

    private void a9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a9Button);
    }//GEN-LAST:event_a9ButtonActionPerformed

    private void a10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(a10Button);
    }//GEN-LAST:event_a10ButtonActionPerformed

    private void b3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b3Button);
    }//GEN-LAST:event_b3ButtonActionPerformed

    private void b4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b4Button);
    }//GEN-LAST:event_b4ButtonActionPerformed

    private void b1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b1Button);
    }//GEN-LAST:event_b1ButtonActionPerformed

    private void b5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b5Button);
    }//GEN-LAST:event_b5ButtonActionPerformed

    private void b6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b6Button);
    }//GEN-LAST:event_b6ButtonActionPerformed

    private void b7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b7Button);
    }//GEN-LAST:event_b7ButtonActionPerformed

    private void b8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b8Button);
    }//GEN-LAST:event_b8ButtonActionPerformed

    private void b9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b9Button);
    }//GEN-LAST:event_b9ButtonActionPerformed

    private void b10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b10Button);
    }//GEN-LAST:event_b10ButtonActionPerformed

    private void b2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(b2Button);
    }//GEN-LAST:event_b2ButtonActionPerformed

    private void c3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c3Button);
    }//GEN-LAST:event_c3ButtonActionPerformed

    private void d5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d5Button);
    }//GEN-LAST:event_d5ButtonActionPerformed

    private void c4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c4Button);
    }//GEN-LAST:event_c4ButtonActionPerformed

    private void d6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d6Button);
    }//GEN-LAST:event_d6ButtonActionPerformed

    private void c5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c5Button);
    }//GEN-LAST:event_c5ButtonActionPerformed

    private void d7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d7Button);
    }//GEN-LAST:event_d7ButtonActionPerformed

    private void c6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c6Button);
    }//GEN-LAST:event_c6ButtonActionPerformed

    private void d8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d8Button);
    }//GEN-LAST:event_d8ButtonActionPerformed

    private void c7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c7Button);
    }//GEN-LAST:event_c7ButtonActionPerformed

    private void d9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d9Button);
    }//GEN-LAST:event_d9ButtonActionPerformed

    private void c8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c8Button);
    }//GEN-LAST:event_c8ButtonActionPerformed

    private void d10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d10Button);
    }//GEN-LAST:event_d10ButtonActionPerformed

    private void c9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c9Button);
    }//GEN-LAST:event_c9ButtonActionPerformed

    private void d2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d2Button);
    }//GEN-LAST:event_d2ButtonActionPerformed

    private void c10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c10Button);
    }//GEN-LAST:event_c10ButtonActionPerformed

    private void c1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c1Button);
    }//GEN-LAST:event_c1ButtonActionPerformed

    private void d3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d3Button);
    }//GEN-LAST:event_d3ButtonActionPerformed

    private void d4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d4Button);
    }//GEN-LAST:event_d4ButtonActionPerformed

    private void c2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(c2Button);
    }//GEN-LAST:event_c2ButtonActionPerformed

    private void d1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(d1Button);
    }//GEN-LAST:event_d1ButtonActionPerformed

    private void e7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e7Button);
    }//GEN-LAST:event_e7ButtonActionPerformed

    private void e8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e8Button);
    }//GEN-LAST:event_e8ButtonActionPerformed

    private void e9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e9Button);
    }//GEN-LAST:event_e9ButtonActionPerformed

    private void e10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e10Button);
    }//GEN-LAST:event_e10ButtonActionPerformed

    private void e2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e2Button);
    }//GEN-LAST:event_e2ButtonActionPerformed

    private void e3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e3Button);
    }//GEN-LAST:event_e3ButtonActionPerformed

    private void e4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e4Button);
    }//GEN-LAST:event_e4ButtonActionPerformed

    private void e1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e1Button);
    }//GEN-LAST:event_e1ButtonActionPerformed

    private void e5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e5Button);
    }//GEN-LAST:event_e5ButtonActionPerformed

    private void e6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_e6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(e6Button);
    }//GEN-LAST:event_e6ButtonActionPerformed

    private void h5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h5Button);
    }//GEN-LAST:event_h5ButtonActionPerformed

    private void i7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i7Button);
    }//GEN-LAST:event_i7ButtonActionPerformed

    private void h6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h6Button);
    }//GEN-LAST:event_h6ButtonActionPerformed

    private void i8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i8Button);
    }//GEN-LAST:event_i8ButtonActionPerformed

    private void h7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h7Button);
    }//GEN-LAST:event_h7ButtonActionPerformed

    private void i9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i9Button);
    }//GEN-LAST:event_i9ButtonActionPerformed

    private void h8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h8Button);
    }//GEN-LAST:event_h8ButtonActionPerformed

    private void i10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i10Button);
    }//GEN-LAST:event_i10ButtonActionPerformed

    private void h9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h9Button);
    }//GEN-LAST:event_h9ButtonActionPerformed

    private void i2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i2Button);
    }//GEN-LAST:event_i2ButtonActionPerformed

    private void h10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h10Button);
    }//GEN-LAST:event_h10ButtonActionPerformed

    private void h1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h1Button);
    }//GEN-LAST:event_h1ButtonActionPerformed

    private void i3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i3Button);
    }//GEN-LAST:event_i3ButtonActionPerformed

    private void i4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i4Button);
    }//GEN-LAST:event_i4ButtonActionPerformed

    private void h2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h2Button);
    }//GEN-LAST:event_h2ButtonActionPerformed

    private void i1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i1Button);
    }//GEN-LAST:event_i1ButtonActionPerformed

    private void f1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f1Button);
    }//GEN-LAST:event_f1ButtonActionPerformed

    private void j7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j7Button);
    }//GEN-LAST:event_j7ButtonActionPerformed

    private void j8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j8Button);
    }//GEN-LAST:event_j8ButtonActionPerformed

    private void j9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j9Button);
    }//GEN-LAST:event_j9ButtonActionPerformed

    private void j10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j10Button);
    }//GEN-LAST:event_j10ButtonActionPerformed

    private void j2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j2Button);
    }//GEN-LAST:event_j2ButtonActionPerformed

    private void f2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f2Button);
    }//GEN-LAST:event_f2ButtonActionPerformed

    private void j3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j3Button);
    }//GEN-LAST:event_j3ButtonActionPerformed

    private void f3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f3Button);
    }//GEN-LAST:event_f3ButtonActionPerformed

    private void j4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j4Button);
    }//GEN-LAST:event_j4ButtonActionPerformed

    private void f4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f4Button);
    }//GEN-LAST:event_f4ButtonActionPerformed

    private void j1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j1Button);
    }//GEN-LAST:event_j1ButtonActionPerformed

    private void f5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f5Button);
    }//GEN-LAST:event_f5ButtonActionPerformed

    private void j5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j5Button);
    }//GEN-LAST:event_j5ButtonActionPerformed

    private void f6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f6Button);
    }//GEN-LAST:event_f6ButtonActionPerformed

    private void j6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(j6Button);
    }//GEN-LAST:event_j6ButtonActionPerformed

    private void f7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f7Button);
    }//GEN-LAST:event_f7ButtonActionPerformed

    private void f8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f8Button);
    }//GEN-LAST:event_f8ButtonActionPerformed

    private void f9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f9Button);
    }//GEN-LAST:event_f9ButtonActionPerformed

    private void f10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(f10Button);
    }//GEN-LAST:event_f10ButtonActionPerformed

    private void g3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g3Button);
    }//GEN-LAST:event_g3ButtonActionPerformed

    private void g4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g4Button);
    }//GEN-LAST:event_g4ButtonActionPerformed

    private void g1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g1ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g1Button);
    }//GEN-LAST:event_g1ButtonActionPerformed

    private void g5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g5Button);
    }//GEN-LAST:event_g5ButtonActionPerformed

    private void g6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g6Button);
    }//GEN-LAST:event_g6ButtonActionPerformed

    private void g7ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g7ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g7Button);
    }//GEN-LAST:event_g7ButtonActionPerformed

    private void g8ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g8ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g8Button);
    }//GEN-LAST:event_g8ButtonActionPerformed

    private void g9ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g9ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g9Button);
    }//GEN-LAST:event_g9ButtonActionPerformed

    private void g10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g10ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g10Button);
    }//GEN-LAST:event_g10ButtonActionPerformed

    private void g2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_g2ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(g2Button);
    }//GEN-LAST:event_g2ButtonActionPerformed

    private void h3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h3ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h3Button);
    }//GEN-LAST:event_h3ButtonActionPerformed

    private void i5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i5ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i5Button);
    }//GEN-LAST:event_i5ButtonActionPerformed

    private void h4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_h4ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(h4Button);
    }//GEN-LAST:event_h4ButtonActionPerformed

    private void i6ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_i6ButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
        reserveSeat(i6Button);
    }//GEN-LAST:event_i6ButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        paintAllSeats();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void finishTransactionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishTransactionButtonActionPerformed
        // TODO add your handling code here:
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1");
            IRemoteSeat rs = (IRemoteSeat) registry.lookup("Seat");
            javax.swing.JToggleButton reservedButton;
            Seat reservedSeat;
            for (int i = 0; i < reservedSeats.size(); i++) {
                reservedButton = (JToggleButton) reservedSeats.get(i);
                reservedSeat = new Seat(reservedButton.getText(), "reserved");
                rs.updateSeat(reservedSeat);
            }
            JOptionPane.showMessageDialog(rootPane, "Cambios guardados",
                    "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
            reservedSeats.clear();
            dispose();
            Login loginForm = new Login();
            loginForm.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_finishTransactionButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(rootPane,
                "¿Esta seguro de que desea cancelar sus reservaciones?",
                "Confirmar acción", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                Registry registry = LocateRegistry.getRegistry("127.0.0.1");
                IRemoteSeat rs = (IRemoteSeat) registry.lookup("Seat");
                IRemoteReservation rr = (IRemoteReservation) registry.lookup("Reservation");
                IRemoteUser ru = (IRemoteUser) registry.lookup("User");
                javax.swing.JToggleButton reservedButton;
                Seat reservedSeat;
                ArrayList <User> arrUser = ru.findUserByName(userFirstName,
                        userLastName);
                int userID = 0;
                for (User u : arrUser) {
                    userID = u.getId();
                }
                for (int i = 0; i < reservedSeats.size(); i++) {
                    reservedButton = (JToggleButton) reservedSeats.get(i);
                    reservedSeat = new Seat(reservedButton.getText(), "available");
                    rs.updateSeat(reservedSeat);
                    ArrayList <Seat> arrSeat = rs.findSeatByName(reservedButton.getText());
                    int seatID = 0;
                    for (Seat s : arrSeat) {
                        seatID = s.getId();
                    }
                    Reservation reservation = new Reservation(userID, seatID);
                    rr.deleteReservation(reservation);
                }
                reservedSeats.clear();
                dispose();
                Login loginForm = new Login();
                loginForm.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SeatSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeatSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeatSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeatSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SeatSelection().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JToggleButton a10Button;
    private static javax.swing.JToggleButton a1Button;
    private static javax.swing.JToggleButton a2Button;
    private static javax.swing.JToggleButton a3Button;
    private static javax.swing.JToggleButton a4Button;
    private static javax.swing.JToggleButton a5Button;
    private static javax.swing.JToggleButton a6Button;
    private static javax.swing.JToggleButton a7Button;
    private static javax.swing.JToggleButton a8Button;
    private static javax.swing.JToggleButton a9Button;
    private static javax.swing.JToggleButton b10Button;
    private static javax.swing.JToggleButton b1Button;
    private static javax.swing.JToggleButton b2Button;
    private static javax.swing.JToggleButton b3Button;
    private static javax.swing.JToggleButton b4Button;
    private static javax.swing.JToggleButton b5Button;
    private static javax.swing.JToggleButton b6Button;
    private static javax.swing.JToggleButton b7Button;
    private static javax.swing.JToggleButton b8Button;
    private static javax.swing.JToggleButton b9Button;
    private static javax.swing.JToggleButton c10Button;
    private static javax.swing.JToggleButton c1Button;
    private static javax.swing.JToggleButton c2Button;
    private static javax.swing.JToggleButton c3Button;
    private static javax.swing.JToggleButton c4Button;
    private static javax.swing.JToggleButton c5Button;
    private static javax.swing.JToggleButton c6Button;
    private static javax.swing.JToggleButton c7Button;
    private static javax.swing.JToggleButton c8Button;
    private static javax.swing.JToggleButton c9Button;
    private javax.swing.JButton cancelButton;
    private static javax.swing.JToggleButton d10Button;
    private static javax.swing.JToggleButton d1Button;
    private static javax.swing.JToggleButton d2Button;
    private static javax.swing.JToggleButton d3Button;
    private static javax.swing.JToggleButton d4Button;
    private static javax.swing.JToggleButton d5Button;
    private static javax.swing.JToggleButton d6Button;
    private static javax.swing.JToggleButton d7Button;
    private static javax.swing.JToggleButton d8Button;
    private static javax.swing.JToggleButton d9Button;
    private static javax.swing.JToggleButton e10Button;
    private static javax.swing.JToggleButton e1Button;
    private static javax.swing.JToggleButton e2Button;
    private static javax.swing.JToggleButton e3Button;
    private static javax.swing.JToggleButton e4Button;
    private static javax.swing.JToggleButton e5Button;
    private static javax.swing.JToggleButton e6Button;
    private static javax.swing.JToggleButton e7Button;
    private static javax.swing.JToggleButton e8Button;
    private static javax.swing.JToggleButton e9Button;
    private static javax.swing.JToggleButton f10Button;
    private static javax.swing.JToggleButton f1Button;
    private static javax.swing.JToggleButton f2Button;
    private static javax.swing.JToggleButton f3Button;
    private static javax.swing.JToggleButton f4Button;
    private static javax.swing.JToggleButton f5Button;
    private static javax.swing.JToggleButton f6Button;
    private static javax.swing.JToggleButton f7Button;
    private static javax.swing.JToggleButton f8Button;
    private static javax.swing.JToggleButton f9Button;
    private javax.swing.JButton finishTransactionButton;
    private static javax.swing.JToggleButton g10Button;
    private static javax.swing.JToggleButton g1Button;
    private static javax.swing.JToggleButton g2Button;
    private static javax.swing.JToggleButton g3Button;
    private static javax.swing.JToggleButton g4Button;
    private static javax.swing.JToggleButton g5Button;
    private static javax.swing.JToggleButton g6Button;
    private static javax.swing.JToggleButton g7Button;
    private static javax.swing.JToggleButton g8Button;
    private static javax.swing.JToggleButton g9Button;
    private static javax.swing.JToggleButton h10Button;
    private static javax.swing.JToggleButton h1Button;
    private static javax.swing.JToggleButton h2Button;
    private static javax.swing.JToggleButton h3Button;
    private static javax.swing.JToggleButton h4Button;
    private static javax.swing.JToggleButton h5Button;
    private static javax.swing.JToggleButton h6Button;
    private static javax.swing.JToggleButton h7Button;
    private static javax.swing.JToggleButton h8Button;
    private static javax.swing.JToggleButton h9Button;
    private static javax.swing.JToggleButton i10Button;
    private static javax.swing.JToggleButton i1Button;
    private static javax.swing.JToggleButton i2Button;
    private static javax.swing.JToggleButton i3Button;
    private static javax.swing.JToggleButton i4Button;
    private static javax.swing.JToggleButton i5Button;
    private static javax.swing.JToggleButton i6Button;
    private static javax.swing.JToggleButton i7Button;
    private static javax.swing.JToggleButton i8Button;
    private static javax.swing.JToggleButton i9Button;
    private static javax.swing.JToggleButton j10Button;
    private static javax.swing.JToggleButton j1Button;
    private static javax.swing.JToggleButton j2Button;
    private static javax.swing.JToggleButton j3Button;
    private static javax.swing.JToggleButton j4Button;
    private static javax.swing.JToggleButton j5Button;
    private static javax.swing.JToggleButton j6Button;
    private static javax.swing.JToggleButton j7Button;
    private static javax.swing.JToggleButton j8Button;
    private static javax.swing.JToggleButton j9Button;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables
}
