package forex.transaction.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class SpotTransaction extends AbstractSpotForwardTransaction {
}
