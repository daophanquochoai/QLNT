package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "communication")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Communication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CommunicationID", nullable = false)
    private int communicationID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SenderID", referencedColumnName = "customerId", nullable = false)
    private Customers senderId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ReceiverID", referencedColumnName = "customerId")
    private Customers receiverID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MessageID", referencedColumnName = "requestsId", nullable = false, unique = true)
    private Requests messageID;
}
