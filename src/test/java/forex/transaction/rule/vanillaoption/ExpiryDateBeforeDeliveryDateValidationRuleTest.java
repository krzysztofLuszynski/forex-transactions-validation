package forex.transaction.rule.vanillaoption;

import forex.transaction.dto.TransactionDTO;
import forex.transaction.dto.vanillaoption.VanillaOptionTransactionDTO;
import forex.transaction.validation.ValidationContext;
import forex.transaction.dto.ValidationErrorDTO;
import forex.transaction.validation.rule.vanillaoption.ExpiryDateBeforeDeliveryDateValidationRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiryDateBeforeDeliveryDateValidationRuleTest {
    ExpiryDateBeforeDeliveryDateValidationRule expiryDateBeforeDeliveryDateValidationRule
            = new ExpiryDateBeforeDeliveryDateValidationRule();

    @Test
    void validateExpiryDateNullDeliveryDateNull() {
        VanillaOptionTransactionDTO vanillaOptionTransaction = new VanillaOptionTransactionDTO();
        ValidationContext<TransactionDTO> validationContext = new ValidationContext<>(vanillaOptionTransaction, 1L);

        Optional<ValidationErrorDTO> validationError = expiryDateBeforeDeliveryDateValidationRule.validate(validationContext);

        assertThat(validationError).isEmpty();
    }

    @Test
    void validateExpiryDateNotNullDeliveryDateNull() {
        VanillaOptionTransactionDTO vanillaOptionTransaction = new VanillaOptionTransactionDTO();
        vanillaOptionTransaction.setExpiryDate(LocalDate.parse("2021-11-11"));
        ValidationContext<TransactionDTO> validationContext = new ValidationContext<>(vanillaOptionTransaction, 2L);

        Optional<ValidationErrorDTO> validationError = expiryDateBeforeDeliveryDateValidationRule.validate(validationContext);

        assertThat(validationError).isEmpty();
    }

    @Test
    void validateExpiryDateNullDeliveryDateNotNull() {
        VanillaOptionTransactionDTO vanillaOptionTransaction = new VanillaOptionTransactionDTO();
        vanillaOptionTransaction.setDeliveryDate(LocalDate.parse("2021-11-11"));
        ValidationContext<TransactionDTO> validationContext = new ValidationContext<>(vanillaOptionTransaction, 3L);

        Optional<ValidationErrorDTO> validationError = expiryDateBeforeDeliveryDateValidationRule.validate(validationContext);

        assertThat(validationError).isEmpty();
    }

    @Test
    void validateExpiryDateBeforeDeliveryDate() {
        VanillaOptionTransactionDTO vanillaOptionTransaction = new VanillaOptionTransactionDTO();
        vanillaOptionTransaction.setExpiryDate(LocalDate.parse("2021-11-10"));
        vanillaOptionTransaction.setDeliveryDate(LocalDate.parse("2021-11-11"));
        ValidationContext<TransactionDTO> validationContext = new ValidationContext<>(vanillaOptionTransaction, 4L);

        Optional<ValidationErrorDTO> validationError = expiryDateBeforeDeliveryDateValidationRule.validate(validationContext);

        assertThat(validationError).isEmpty();
    }

    @Test
    void validateExpiryDateEqualDeliveryDate() {
        VanillaOptionTransactionDTO vanillaOptionTransaction = new VanillaOptionTransactionDTO();
        vanillaOptionTransaction.setExpiryDate(LocalDate.parse("2021-11-11"));
        vanillaOptionTransaction.setDeliveryDate(LocalDate.parse("2021-11-11"));
        ValidationContext<TransactionDTO> validationContext = new ValidationContext<>(vanillaOptionTransaction, 5L);

        Optional<ValidationErrorDTO> validationError = expiryDateBeforeDeliveryDateValidationRule.validate(validationContext);

        assertThat(validationError).isNotEmpty();
        assertThat(validationError.get()).hasFieldOrPropertyWithValue("transactionNumber", 5L);
        assertThat(validationError.get().getAffectedFields()).containsExactly("expiryDate", "deliveryDate");
        assertThat(validationError.get()).hasFieldOrPropertyWithValue("message",
                "Expiry date: 2021-11-11, shall be before delivery date: 2021-11-11");
    }

    @Test
    void validateExpiryDateAfterDeliveryDate() {
        VanillaOptionTransactionDTO vanillaOptionTransaction = new VanillaOptionTransactionDTO();
        vanillaOptionTransaction.setExpiryDate(LocalDate.parse("2021-11-12"));
        vanillaOptionTransaction.setDeliveryDate(LocalDate.parse("2021-11-11"));
        ValidationContext<TransactionDTO> validationContext = new ValidationContext<>(vanillaOptionTransaction, 6L);

        Optional<ValidationErrorDTO> validationError = expiryDateBeforeDeliveryDateValidationRule.validate(validationContext);

        assertThat(validationError).isNotEmpty();
        assertThat(validationError.get()).hasFieldOrPropertyWithValue("transactionNumber", 6L);
        assertThat(validationError.get().getAffectedFields()).containsExactly("expiryDate", "deliveryDate");
        assertThat(validationError.get()).hasFieldOrPropertyWithValue("message",
                "Expiry date: 2021-11-12, shall be before delivery date: 2021-11-11");
    }
}
