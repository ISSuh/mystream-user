package mystream.user.service.external;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import mystream.user.dto.NewStreamDto;

@Service
@RequiredArgsConstructor
public class BroadcastServiceProducer {

  static private final String TOPIC_CREATE_STREAM = "create-stream";

  private final KafkaTemplate<String, NewStreamDto> kafkaTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void createStream(NewStreamDto newStreamDto) {
    // ToStringSerializer<NewStreamDto> serializer = new ToStringSerializer<>();
    // String json = null;
    // try {
    //   json = objectMapper.writeValueAsString(newStreamDto);
    // } catch(JsonProcessingException e) {
    //   throw new InvalidSignupException("invalid signup value");
    // }

    // serializer.serialize(TOPIC_CREATE_STREAM, newStreamDto)
    kafkaTemplate.send(TOPIC_CREATE_STREAM, newStreamDto);
  }
}
