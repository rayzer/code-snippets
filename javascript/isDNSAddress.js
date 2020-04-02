function isDnsAddress(value) {
    if (!(typeof value === 'string' || value instanceof String)) {
        return false
    }

    var valueTrimmed = value.trim();
    if (valueTrimmed.length === 0)  {
        return false;
    }

    //Validation rule sync with java InternetDomainName class:
    //Domain name is composed of a series of labels concatenated with dots. ( or '。' and other dot forms, not implemented)
    //Domain name can end with . but can not start with .
    //Domain name max length is 253 (not counting the last trailing dot)
    //Max number of label is 127
    //Each label is 1 to 63 characters long, and may contain:
    //    the ASCII letters a-z (in a case insensitive manner),
    //    the digits 0-9,
    //    and the hyphen ('-') or underscore ('_').
    //labels cannot start or end with hyphens (RFC 952) or underscore
    //labels can start with numbers (RFC 1123), last label can not start with number
    //Note: only one label is also supported, e.g. localhost

    var valueTrimmed_remove_trailing_dot = valueTrimmed.replace(/\.$/, "");
    if (valueTrimmed_remove_trailing_dot.length > 253)  {
        return false;
    }

    var dns_regex = /^(([a-zA-Z\d]([a-zA-Z\d\-_]{0,61}))?[a-zA-Z0-9]\.){0,126}(?!\d+)(?!-)(?!_)[a-zA-Z0-9-_]{0,62}[a-zA-Z0-9]\.?$/;
    var ipv4_regex2 = /^\d+(\.\d*)+\s*$/;
    //e.g. 1.1.1.300 is not DNS and IPV4
    return null != dns_regex.exec(valueTrimmed) && null == ipv4_regex2.exec(valueTrimmed);
}