
function sanitizeHTML(string, allowedTags, forbiddenTags) {
  if (!allowedTags) allowedTags = ["b", "i", "p", "br"];
  if (!forbiddenTags) forbiddenTags = ["script", "object", "embed"];

  var white = allowedTags.join("|");
  var black = forbiddenTags.join("|");

  var e = new RegExp("(<("+black+")[^>]*>.*</\\2>|(?!<[/]?("+white+")(\\s[^<]*>|[/]>|>))<[^<>]*>|(?!<[^<>\\s]+)\\s[^</>]+(?=[/>]))", "gi");
  return string.replace(e, "");
}