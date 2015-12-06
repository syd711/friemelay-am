<#include "includes/header.ftl">

<b>Bestellbestätigung / Rechnung Nr. ${order.formattedId} vom ${order.formattedCreationDate}</b><br>
<br>
<br>
Guten Tag,<br>
<br>
vielen Dank für Ihre Bestellung bei der <b>Friemelay</b>!<br>
<br>
Die von Ihnen bestellten Waren sind	vorrätig, somit nehmen wir hiermit Ihre Bestellung an und werden sie unverzüglich nach Erhalt der Begleichung der Rechnungssumme an Sie ausliefern.<br>
<br>
Ihre Bestellung geht an:<br>
<br>
${address.firstname.value} ${address.lastname.value}<br>
<#if address.company?has_content>
    ${address.company.value}<br>
</#if>
<#if address.additional?has_content>
    ${address.additional.value}<br>
</#if>
${address.street.value}<br>
${address.zip.value}<br>
${address.city.value}<br>
<#if address.country?has_content>
    ${address.country.value}<br>
</#if>
 <br>

Versandart: Standardversand DHL<br>
<br>
<#include "includes/order-table.ftl">

Bitte richten Sie Ihre Überweisungen mit dem Verwendungzweck "Bestellung ${order.formattedId}" an die:<br>
<br>
Volksbank Beckum-Lippstadt<br>
Inhaber: Thorsten Faust<br>
Konto-Nr.: ${account}<br>
BLZ: ${blz}<br>
BIC: ${bic}<br>
IBAN: ${iban}<br>
<br>

Wir werden Sie umgehend benachrichtigen, sobald die von Ihnen bei uns bestellten Waren versandt wurden.<br>
<br>
Die Lieferung erfolgt unverzüglich nach Zahlungseingang. Bitte beachten Sie, dass die an Sie versandten Waren bis zu 7 Werktage auf dem "Postweg" unterwegs sein können.<br>
<br>
Sollte der o.g. Betrag innerhalb der 10 Tages-Frist nicht überwiesen werden, gilt ihre Bestellung als ungültig.<br>
<br>
Unsere AGB finden Sie auf der unserer Homepage unter <a href="http://www.friemelay.de">www.friemelay.de.</a><br>
<br>
Wir wünschen Ihnen viel Spaß mit unseren Waren und laden Sie gerne wieder dazu ein in unserem Sortiment zu stöbern.<br>
<br>


<#include "includes/footer.ftl">