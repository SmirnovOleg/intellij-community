enum class E {
    A
}

fun use() {
    E.valueOf<caret>("A")
}


//INFO: <div class='definition'><pre><a href="psi_element://E"><code style='font-size:96%;'>E</code></a><br>public final fun <b>valueOf</b>(
//INFO:     value: String
//INFO: ): <a href="psi_element://E">E</a></pre></div><div class='content'><p>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.)</p></div><table class='sections'><tr><td valign='top' class='section'><p>Throws:</td><td valign='top'><p><code><a href="psi_element://IllegalArgumentException"><code style='font-size:96%;'>IllegalArgumentException</code></a></code> - if this enum type has no constant with the specified name</td></table>
