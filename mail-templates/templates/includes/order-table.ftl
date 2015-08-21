<b>Einzeilheiten Ihrer Bestellung:</b><br>
  <br>
     <table border="0" cellpadding="6" cellspacing="0" style="font-family:Verdana;font-size:12px;">
      <tr>
        <td></td>
        <td><b>Name</b></td>
        <td><b>Anzahl</b></td>
        <td><b>Einzelpreis</b></td>
        <td align="right"><b>Preis</b></td>
      </tr>

         <#list order.orderItems as item>
            <tr>
                <td><a href="${item.url}">
                    <img style="border: 1px solid #d5d5d5;padding:3px;" width="120" src="${item.imageUrl}" alt="${item.productDescription}"></a>
                </td>
                <td><a href="${item.url}"><b>${item.productDescription}</b></a></td>
                <td>${item.amount.value}</td>
                <td>${item.price} Euro</td>
                <td align="right">${item.formattedTotalPrice} EUR</td>
            </tr>
        </#list>
        <tr>
            <td colspan="5" align="right"><hr></td>
        </tr>
        <tr>
            <td colspan="5" align="right">Zwischensumme: ${order.formattedTotalPrice} EUR</td>
        </tr>
        <tr>
            <td colspan="5" align="right">Versandkosten*: ${order.formattedShippingCosts} EUR</td>
        </tr>
        <tr>
        <td colspan="5" align="right"><hr></td>
        </tr>
        <tr>
        <td colspan="5" align="right"><b>Gesamt: ${order.formattedTotalPriceWithShipping} EUR</b></td>
        </tr>
        <tr>
        <td colspan="5" align="right"><hr></td>
        </tr>
        <tr>
        <td colspan="5" align="right"><p style="font-size:10px;">(Gem&auml;&szlig; §19 UstG (Kleinunternehmerregelung) entf&auml;llt die Umsatzsteuer.)</p></td>
        </tr>
        <tr>
        <td colspan="5" align="right"><p style="font-size:10px;">*Die angegebenen Versandkosten gelten nur f&uuml;r den Versand innerhalb Deutschlands. Bei Versand ins Ausland k&ouml;nnen die Versandkosten davon abweichen.</p></td>
        </tr>
    </table>
<br>
<br>