package ewmservice.request.repository;

import ewmservice.request.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r WHERE r.requester=:user_id")
    List<Request> findAllByRequester(@Param("user_id") Long userId);

    @Query(value = "SELECT * FROM requests r " +
            "LEFT OUTER JOIN events e on e.id = r.event_id " +
            "WHERE e.initiator_id = :user_id " +
            "AND r.event_id = :event_id", nativeQuery = true)
    List<Request> getInfoAboutRequestsForEventOwner(@Param("user_id") Long userId,
                                                    @Param("event_id") Long eventId);

    @Query(value = "SELECT * FROM requests r " +
            "LEFT JOIN events e ON e.id = r.event_id " +
            "WHERE r.id = :id AND event_id = :event_id AND initiator_id = :user_id", nativeQuery = true)
    Request findByInitiatorAndRequestAndEvent(@Param("id") Long reqId,
                                              @Param("user_id") Long userId,
                                              @Param(("event_id")) Long eventId);

    @Query("SELECT r FROM Request r WHERE r.id=:req_id AND r.requester=:user_id")
    Request findRequestForRequester(@Param("user_id") Long userId,
                                    @Param("req_id") Long reqId);

    Request findRequestByEventIdAndRequester(@Param("event_id") Long eventId, @Param("requester") Long userId);
}
