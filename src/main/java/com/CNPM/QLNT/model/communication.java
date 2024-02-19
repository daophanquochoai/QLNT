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
    @Column(name = "CommunicationID")
    private int CommunicationID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SenderID_id", referencedColumnName = "customer_id")
    private customer SenderId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ReceiverID_id", referencedColumnName = "customer_id")
    private customer ReceiverID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MessageID_id", referencedColumnName = "requests_id")
    private requests MessageID;
}
