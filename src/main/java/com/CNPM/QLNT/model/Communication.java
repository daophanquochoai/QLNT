package com.CNPM.QLNT.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

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

    @ManyToOne()
    @Cascade({org.hibernate.annotations.CascadeType.DETACH,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH})
    @JoinColumn(name = "SenderID", referencedColumnName = "customerId", nullable = false)
    private Customers senderId;

    @ManyToOne()
    @Cascade({org.hibernate.annotations.CascadeType.DETACH,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH})
    @JoinColumn(name = "ReceiverID", referencedColumnName = "customerId")
    private Customers receiverID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MessageID", referencedColumnName = "requestsId", nullable = false, unique = true)
    private Requests messageID;
}
