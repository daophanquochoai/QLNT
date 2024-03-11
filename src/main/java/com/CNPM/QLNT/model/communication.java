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
public class communication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CommunicationID", nullable = false)
    private int communicationID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SenderID", referencedColumnName = "customerId")
    private customer senderId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ReceiverID", referencedColumnName = "customerId")
    private customer receiverID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MessageID", referencedColumnName = "requestsId", nullable = false)
    private requests messageID;
}
