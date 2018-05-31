<#include "includes/header.ftl">

<b>Bestellbestätigung / Rechnung Nr. ${order.formattedId} vom ${order.formattedCreationDate}</b><br>
<br>
<br>
Empfänger:<br>
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