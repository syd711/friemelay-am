<#include "includes/header.ftl">

Guten Tag ${name},<br>
<br>
Ihre Bestellung wurde heute von uns bearbeitet und ist nun auf dem Weg zu Ihnen. Die Lieferzeit beträgt ca. 2-3 Werktage.<br>
<br>
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
<#include "includes/footer.ftl">

